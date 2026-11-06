# Changelog

All notable changes to **Fallback** are documented here.

## [1.0.0] - 2026-11-06
### Added
- Moves players to a configured fallback server when the server they are on goes offline
  (crash, timeout, connection error, kick or failed transfer).
- Ordered fallback list (`servers`) with optional use of the proxy `listener.priorities`.
- Offline tracking (`down-cooldown-seconds`) to prevent loops and avoid dead servers.
- Selectable disconnect reasons (`reasons`).
- `stay-on-transfer-failure` option.
- Configurable message after the player was moved (`messages.fallback`).
- Config validation against the proxy server list.
