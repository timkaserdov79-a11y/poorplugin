# PoorPlugin

Anti-cheat punishment system для Paper 1.20.4+

## Возможности

- ✅ `/poor players` - Показать всех игроков с модом Sipper
- ✅ `/poor nakaz <ник>` - Выдать слепоту + замедление игроку
- ✅ `/poor unakaz <ник>` - Снять наказание
- ✅ Полная синхронизация с клиентским модом
- ✅ Конфигурируемый список забанены модулей

## Установка

1. Скачай готовый `.jar` файл
2. Поменяй расширение `.zip` на `.jar` (если нужно)
3. Кинь в папку `plugins/`
4. Перезагрузи сервер

## Конфиг

Плагин автоматически создаст `config.yml` в папке `plugins/PoorPlugin/`

```yaml
banned-modules:
  - GuiMove
  - ItemScroller
  - NoDelay
  # ... и другие

effects:
  blindness-duration: 600  # 30 сек
  slowness-duration: 600
  slowness-level: 2
```

## Команды

- `/poor players` - Список игроков с модом
- `/poor nakaz <ник>` - Наказать игрока
- `/poor unakaz <ник>` - Отпустить игрока

## Версия

- Paper 1.20.4+
- Java 17+

## Автор

PoorTeam
