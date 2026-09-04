# DarkBlocks

DarkBlocks was a German-language Minecraft network built in 2019: a BungeeCord
proxy core plus a set of Spigot minigame and lobby plugins, tied together by a
shared Java library. This repository is published as a historical reference —
it is **unmaintained** and the code reflects 2019-era Java and Minecraft
versions.

> [!WARNING]
> Do not run this on a modern server unmodified. It targets old Spigot /
> BungeeCord APIs and depends on the third-party `SegdoCloudPlugin`, which is
> **not** included in this repository.

## Modules

| Module | Type | Description |
|---|---|---|
| `Core` | BungeeCord + Spigot | Proxy core: auto-ban, auto-messages, blocked commands, coins, permissions; includes the lobby-side Spigot plugin |
| `BedWars` | Spigot minigame | BedWars with countdown and match lifecycle handling |
| `GunGame` | Spigot minigame | GunGame progression (weapon tiers per kill) |
| `Cores` | Spigot minigame | Team "destroy the core" minigame with core damage events and countdowns |
| `Lobby` | Spigot | Lobby server: cosmetics, sounds, daily rewards, case opening, and a Cookie Clicker easter egg |
| `DarkApplicationProgrammingInterface` | Shared library | MySQL-backed coins/stats APIs, file-based configuration, UUID/name fetchers, license checks |
| `DarkCloud` | Infrastructure | Bootstrap component for cloud-based server management |

## Database

Credentials are read at runtime from a properties file generated on first
start — no secrets are stored in this repository.

## License

[MIT](LICENSE)
