package nuclear.control.handler.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import io.netty.buffer.Unpooled;
import mods.viaversion.vialoadingbase.ViaLoadingBase;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CCustomPayloadPacket;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraft.network.play.server.SJoinGamePacket;
import net.minecraft.util.ResourceLocation;
import nuclear.control.events.EventManager;
import nuclear.control.events.impl.packet.EventPacket;
import nuclear.control.events.impl.player.EventUpdate;
import nuclear.control.Manager;
import nuclear.module.api.Module;
import nuclear.utils.ClientUtils;

import java.util.HashSet;
import java.util.Set;

import static nuclear.utils.IMinecraft.mc;

public class ServerAuthHandler {

    private static final ResourceLocation AUTH_CHANNEL = new ResourceLocation("siv", "auth");
    private static final String AUTH_TOKEN = "alphabravo";
    private static final String RESPONSE_TOKEN = "charliedelta";

    private static final Set<String> blockedModules = new HashSet<>();
    private static final Gson gson = new Gson();
    private static final long TIMEOUT_MS = 15000L;

    private boolean joinFlag;
    private boolean sentAuth;
    private boolean waitingResponse;
    private long authStartTime;
    private boolean authConfirmed = false;
    private int joinGameTick = 0;

    public ServerAuthHandler() {
        EventManager.register(this);
    }

    public void onPacket(EventPacket e) {
        if (e.isReceive()) {
            if (e.getPacket() instanceof SJoinGamePacket) {
                reset();
                joinFlag = true;
                joinGameTick = 0;
                ClientUtils.sendMessage("§8[§aAuth§8] §7Подключение...");
            } else if (e.getPacket() instanceof SCustomPayloadPlayPacket) {
                SCustomPayloadPlayPacket p = (SCustomPayloadPlayPacket) e.getPacket();
                // Проверяем только если это наш канал
                if (p.getChannelName().equals(AUTH_CHANNEL)) {
                    handleResponse(p);
                }
            }
        }
    }

    public void onUpdate(EventUpdate e) {
        if (!joinFlag || mc.player == null || mc.player.connection == null) return;

        joinGameTick++;

        // Если уже авторизованы - выходим
        if (authConfirmed) {
            return;
        }

        // Отправляем токен авторизации (максимум один раз)
        if (!sentAuth) {
            try {
                PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
                buf.writeString(AUTH_TOKEN);
                mc.player.connection.sendPacket(new CCustomPayloadPacket(AUTH_CHANNEL, buf));
                sentAuth = true;
                waitingResponse = true;
                authStartTime = System.currentTimeMillis();
                ClientUtils.sendMessage("§8[§aAuth§8] §7Проверяю...");
            } catch (Exception e1) {
                // Сервер не поддерживает - продолжаем
                ClientUtils.sendMessage("§8[§aAuth§8] §7Готово!");
                authConfirmed = true;
            }
            return;
        }

        // Проверяем таймаут (15 сек)
        if (waitingResponse && System.currentTimeMillis() - authStartTime > TIMEOUT_MS) {
            waitingResponse = false;
            ClientUtils.sendMessage("§8[§aAuth§8] §7Готово!");
            authConfirmed = true;
        }

        // Блокируем модули которые запретил сервер
        if (!blockedModules.isEmpty()) {
            for (String name : blockedModules) {
                Module mod = Manager.FUNCTION_MANAGER.get(name);
                if (mod != null && mod.isState()) {
                    mod.setState(false);
                }
            }
        }
    }

    private void handleResponse(SCustomPayloadPlayPacket p) {
        try {
            PacketBuffer data = p.getBufferData();
            if (data == null || data.readableBytes() == 0) {
                authConfirmed = true;
                waitingResponse = false;
                return;
            }

            String text = data.readString(32767).trim();

            // Проверяем что это ответ с нашим токеном
            if (!text.startsWith(RESPONSE_TOKEN)) {
                authConfirmed = true;
                waitingResponse = false;
                return;
            }

            // Извлекаем JSON
            String jsonPart = text.substring(RESPONSE_TOKEN.length()).trim();
            
            if (jsonPart.isEmpty()) {
                // Пустой ответ - модули не запрещены
                ClientUtils.sendMessage("§8[§aAuth§8] §aОК!");
                waitingResponse = false;
                authConfirmed = true;
                sendConfirmation();
                return;
            }

            try {
                JsonObject obj = gson.fromJson(jsonPart, JsonObject.class);
                
                if (obj != null && obj.has("banned")) {
                    var banned = obj.getAsJsonArray("banned");
                    
                    if (banned.size() == 0) {
                        // Нет запрещенных модулей
                        ClientUtils.sendMessage("§8[§aAuth§8] §aОК!");
                    } else {
                        // Есть запрещенные модули
                        StringBuilder list = new StringBuilder();
                        banned.forEach(el -> {
                            String modName = el.getAsString();
                            blockedModules.add(modName);
                            if (list.length() > 0) list.append("§7, ");
                            list.append("§c").append(modName);
                            
                            // Сразу отключаем модуль
                            Module mod = Manager.FUNCTION_MANAGER.get(modName);
                            if (mod != null && mod.isState()) {
                                mod.setState(false);
                            }
                        });
                        ClientUtils.sendMessage("§8[§aAuth§8] §7Отключены: " + list);
                    }
                    
                    waitingResponse = false;
                    authConfirmed = true;
                    sendConfirmation();
                    
                } else {
                    // Нет поля banned - не блокируем
                    waitingResponse = false;
                    authConfirmed = true;
                }
            } catch (Exception ex) {
                // Ошибка JSON - не критично, продолжаем
                waitingResponse = false;
                authConfirmed = true;
            }

        } catch (Exception e) {
            // Любая ошибка - не блокируем игрока
            waitingResponse = false;
            authConfirmed = true;
        }
    }

    private void sendConfirmation() {
        try {
            if (mc.player != null && mc.player.connection != null) {
                PacketBuffer confirmBuf = new PacketBuffer(Unpooled.buffer());
                confirmBuf.writeString("CONFIRMED");
                mc.player.connection.sendPacket(new CCustomPayloadPacket(AUTH_CHANNEL, confirmBuf));
            }
        } catch (Exception ignored) {}
    }

    private void reset() {
        joinFlag = false;
        sentAuth = false;
        waitingResponse = false;
        authStartTime = 0L;
        authConfirmed = false;
        blockedModules.clear();
        joinGameTick = 0;
    }

    public static boolean isModuleBlocked(String name) {
        return blockedModules.contains(name);
    }

    public static boolean isServerWithAuth() {
        return false;
    }
}
