# 📖 Инструкция по установке и использованию PoorPlugin

## ✅ Требования

- **Paper 1.20.4+** (или любой Bukkit-совместимый сервер)
- **Java 17+**
- **Maven** (для сборки из исходников)

## 🚀 Быстрая установка

### Вариант 1: Скачать готовый плагин

1. Перейди в раздел **Releases** этого репозитория
2. Скачай файл `poorplugin-1.0.0.jar`
3. Переименуй расширение если нужно: `.zip` → `.jar`
4. Положи файл в папку **`plugins/`** на твоем сервере
5. Перезагрузи сервер командой `/reload`

### Вариант 2: Собрать из исходников

#### Linux/Mac:
```bash
# Клонируем репо
git clone https://github.com/timkaserdov79-a11y/poorplugin.git
cd poorplugin

# Собираем плагин
chmod +x build.sh
./build.sh

# Готовый файл в: target/poorplugin-1.0.0.jar
```

#### Windows:
```bash
# Клонируем репо
git clone https://github.com/timkaserdov79-a11y/poorplugin.git
cd poorplugin

# Собираем плагин
build.bat

# Готовый файл в: target\poorplugin-1.0.0.jar
```

## 📋 Команды

```
/poor players           # Показать всех игроков с модом Sipper
/poor nakaz <ник>       # Выдать слепоту + замедление игроку
/poor unakaz <ник>      # Снять наказание
```

**Требуемое право:** `poor.admin` (по умолчанию только ops)

## ⚙️ Конфигурация

Плагин автоматически создаст файл `plugins/PoorPlugin/config.yml` при первом запуске.

```yaml
# Список забанены модулей
banned-modules:
  - GuiMove
  - ItemScroller
  - NoDelay
  - ObsidianFarm
  # ... и еще много других

# Настройки эффектов наказания
effects:
  blindness-duration: 600   # Длительность слепоты (30 сек)
  slowness-duration: 600    # Длительность замедления (30 сек)
  slowness-level: 2         # Уровень замедления (1-4)
```

## 🔄 Синхронизация с клиентским модом

Плагин автоматически:
1. Отправляет список забанены модулей клиенту через канал `siv:auth`
2. Получает подтверждение `CONFIRMED` когда клиент отключит читы
3. Применяет эффекты если модули остаются активными

## 🛠️ Структура проекта

```
poorplugin/
├── src/main/java/ru/poor/plugin/
│   ├── PoorPlugin.java              # Основной класс плагина
│   ├── commands/
│   │   └── PoorCommand.java         # Обработка команд
│   ├── listeners/
│   │   └── PluginListener.java      # Слушатели событий
│   └── managers/
│       ├── PunishmentManager.java   # Управление наказаниями
│       └── ConfigManager.java       # Управление конфигом
├── pom.xml                          # Maven конфигурация
├── plugin.yml                       # Описание плагина
└── build.sh / build.bat             # Скрипты сборки
```

## 📝 Пример использования

```
# Показать игроков с модом
> /poor players
[PoorPlugin] 
• PlayerName1 (uuid-here)
• PlayerName2 (uuid-here)
Всего: 2 игроков

# Наказать игрока
> /poor nakaz PlayerName1
[PoorPlugin] Игрок PlayerName1 получил наказание!

# Плагин отправляет слепоту + замедление и список забанены модулей

# Когда игрок отключит читы
> /poor unakaz PlayerName1
[PoorPlugin] Игрок PlayerName1 свободен от наказания!
```

## 🔧 Разработка

Если хочешь добавить свои модули в список забанены:

1. Отредактируй `src/main/java/ru/poor/plugin/managers/ConfigManager.java`
2. Добавь модуль в список в методе `createDefaultConfig()`
3. Пересобери плагин: `./build.sh` или `build.bat`

## 📞 Поддержка

Если у тебя есть вопросы или баги - создай **Issue** в этом репозитории!

---

**Автор:** PoorTeam  
**Версия:** 1.0.0  
**Лицензия:** MIT
