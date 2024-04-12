package blue.lhf.mini_info;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.text.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.logging.Level;

import static blue.lhf.mini_info.MiniInfo.Permissions.*;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText;

public final class MiniInfo extends JavaPlugin implements Listener {

    public static class Permissions {
        private Permissions() {}
        public static final String SHOW_MOTD_ON_JOIN = "mini-info.show_motd_on_join";
        public static final String SHOW_MOTD_ON_COMMAND = "mini-info.show_motd_on_command";
        public static final String SEES_GUIDE = "mini-info.sees_guide";
        public static final String SET_MOTD = "mini-info.set_motd";
    }

    private Path motdPath;
    private MiniMessage miniMessage;

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        try {
            if (!Files.exists(motdPath)) {
                if (!player.hasPermission(SEES_GUIDE)) return;
                player.sendMessage(getGuide());
                return;
            }

            if (!player.hasPermission(SHOW_MOTD_ON_JOIN)) return;
            player.sendMessage(getMotd());
        } catch (final IOException exception) {
            getLogger().log(Level.WARNING, "Failed to send MOTD to player %s"
                    .formatted(plainText().serialize(player.displayName())), exception);
        }
    }

    @Override
    public void onEnable() {
        final Path dataPath = getDataFolder().toPath();

        try {
            Files.createDirectories(dataPath);
        } catch (final IOException exception) {
            getLogger().log(Level.SEVERE, "Failed to create plugin directory!", exception);
        }

        this.motdPath = dataPath.resolve("MOTD.txt");
        this.miniMessage = MiniMessage.miniMessage();

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @NotNull
    private Component getMotd() throws IOException {
        return MiniMessage.miniMessage().deserialize(Files.readString(motdPath));
    }

    @NotNull
    private Component getGuide() throws IOException {
        final InputStream resource = getResource("GUIDE");
        if (resource == null) throw new NoSuchFileException("GUIDE", null,
                "The plugin resource containing the default MOTD could not be found");

        return miniMessage.deserialize(new String(resource.readAllBytes()));
    }

    @Override
    public boolean onCommand(
            @NotNull final CommandSender sender, @NotNull final Command command,
            @NotNull final String label, @NotNull final String[] args) {


        try {
            if (args.length == 0) {
                if (!sender.hasPermission(SHOW_MOTD_ON_COMMAND)) {
                    sender.sendMessage(text("You are not allowed to see the MOTD using a command!").color(RED));
                    return true;
                }

                sender.sendMessage(Files.exists(motdPath) ? getMotd() : getGuide());
                return true;
            }

            if (!sender.hasPermission(SET_MOTD)) {
                sender.sendMessage(text("You are not allowed to set the MOTD!").color(RED));
                return true;
            }

            final String motdInput = String.join(" ", args);

            // Modern StringEscapeUtils also does octal etc.
            Files.writeString(motdPath, StringEscapeUtils.unescapeJava(motdInput));

            sender.sendMessage(empty().append(
                    empty().color(GREEN)
                            .append(text("Successfully set the MOTD!")).appendNewline()
                            .append(text("Here's a preview:")).appendNewline()
                    ));

            sender.sendMessage(getMotd());
        } catch (final IOException exception) {
            sender.sendMessage(empty().color(RED)
                    .append(text("An exception occurred. That's bad!")).appendNewline()
                    .append(text("If you're an administrator, check the console for details.")));
            getLogger().log(Level.SEVERE, "Failed to process command: /" + label + " " + String.join(" ", args), exception);
        }

        return true;
    }
}
