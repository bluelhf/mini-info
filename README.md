![mini-info](assets/logo.png)

**mini-info** is a [Paper](https://github.com/PaperMC/Paper) plugin for showing text to players when they join.

## Installation

1. Download the latest release from the [releases page](https://github.com/bluelhf/mini-info/releases).
2. Drop the `.jar` file into your `plugins` folder.
3. Restart your server.
4. Create a file called `MOTD.txt` in `plugins/mini-info/` and put your message in there.
5. Profit :) No need to restart, reload, or anything like that... it just works.

### MiniMessage Support

![mini-info supports MiniMessage, which lets you do lots of cool stuff, like rainbows! See the documentation for more.](assets/guide.png)

> **Note**  
> You can find the documentation for MiniMessage [on their website](https://docs.advntr.dev/minimessage/format.html#minimessage-format) :)

### The `/motd` command

You can also see or edit the MOTD in-game with the `/motd` command.

Usage:

- `/motd` - Shows the current MOTD.
- `/motd <message>` - Sets the MOTD to `<message>`. MiniMessage and standard Java escape sequences (`\n`, `\074`, `\U+00F7`, etc.) are supported.

### Permissions

| Permission                       | Description                                                             | Default              |
|----------------------------------|-------------------------------------------------------------------------|----------------------|
| `mini-info.show_motd_on_join`    | Allows the player to see the message of the day when they join          | Allowed for everyone |
| `mini-info.show_motd_on_command` | Allows the player to see the message of the day using the /motd command | Allowed for everyone |
| `mini-info.set_motd`             | Allows the player to set the message of the day                         | Operators only       |
| `mini-info.sees_guide`           | Allows the player to see the plugin guide if no MOTD is set             | Operators only       |