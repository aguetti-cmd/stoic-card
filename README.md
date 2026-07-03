# Stoic Card

A small native Android app in the Nothing design language: one Stoic quote per day, and a sorter that files today's friction into what is in your control and what is not.

Built as a proof of concept for AI-assisted app development: plain Java, fully programmatic UI, zero dependencies, no backend, no analytics.

## Features

- One quote per calendar date (366 including Feb 29), drawn from public-domain translations of Marcus Aurelius, Epictetus, Seneca, and Musonius Rufus, organized under 12 monthly themes (Clarity, Emotion, Awareness, Judgment, Action, Obstacles, Duty, Pragmatism, Courage, Virtue, Acceptance, Mortality)
- Evening page: one journal entry per day with a rotating prompt tied to the month's theme; entries are kept indefinitely
- Friction sorter: type what is on your mind, then file it as "in my control" (actionable, with a checkbox) or "not mine" (released, struck through)
- History tab: read-only view of past journal pages (all time) and friction entries (last 30 days) with per-day counts
- Streak counter with a 14-day dot row
- Optional morning notification at 08:00 with the quote of the day (local alarm, survives reboot)
- Day-of-year header drawn by a custom 5x7 dot-matrix view, monochrome palette with a red accent

## Stack

- Plain Java, single Activity plus a broadcast receiver, UI built entirely in code
- No third-party dependencies; persistence via SharedPreferences (30-day retention for friction entries, journal pages kept forever)
- minSdk 23, targetSdk 36
- Permissions: `POST_NOTIFICATIONS` and `RECEIVE_BOOT_COMPLETED`, both serving the morning quote

## Build

Requires JDK 17+ and the Android SDK (platform 36).

```
gradle assembleDebug
```

The APK lands in `app/build/outputs/apk/debug/app-debug.apk`. Create a `local.properties` pointing `sdk.dir` at your Android SDK. Note that `gradle.properties` pins `org.gradle.java.home` to a Homebrew JDK path; adjust or remove that line for your machine.

## License

MIT, see [LICENSE](LICENSE).
