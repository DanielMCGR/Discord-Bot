package bot;

import utils.Calculator;
import utils.YeeLight;
import com.mollin.yapi.enumeration.YeelightProperty;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

// Admin Commands:  .kb Default  .kb Nick  .kb Avatar  .kb data
// User Commands:  .kb What is  .kb Self Destruct  .kb  .kb Join Date  .kb Get Date  .kb Get Hour  .kb Get Time  .kb Hello Kebbot  .kb ping  .kb Help  .kb Come Here

/**
 * This class stores almost all commands and executes them.
 * This also makes it easier to read the commands and the code
 * <p>
 * Note: Read the JDA documentation
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Bot
 * @see Command
 * @see net.dv8tion.jda.api.JDA
 */

public class CommandList {
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
    private static HashMap<String, Command> commandList = new HashMap<String, Command>() {{
        //first are all the admin/owner commands
        put("default", new Command("default", true, "Changes the profile picture of the bot to the default one\nExample Usage: .kb default"));
        put("nick", new Command("nick", true, "Changes the nickname of the bot to a different one\nExample Usage: .kb nick new_nick"));
        put("avatar", new Command("avatar", true, "Changes the avatar of the bot to a different one\nExample Usage: .kb avatar URL_of_Avatar"));
        put("data", new Command("data", true, "Gets some data from the server so the bot can work better :D\nExample Usage: .kb data"));
        put("ping", new Command("ping", true, "Pings the bot, getting the response in ms\nExample Usage: .kb ping"));
        put("lights", new Command("lights", true, "Gives some options to control lights (Yeelight)\nExample Usage: .kb lights"));
        //these are the other commands, reminder that the constructor for the command class is: (Long[] allowedServersID, Long[] allowedTextChannelsID, Long[] roleIDs, Long[] userIDs, String[] aliases, String name, boolean enabled, boolean canDisable)
        put("what_is", new Command("what_is", false, "Basic calculator\n Example usage: .kb what_is 5*1  (Use: + - * / ^ )"));
        put("meter", new Command("meter", false, "Measure how much of something someone is!\n Example usage: .kb meter Happy @User"));
        put("self_destruct", new Command("self_destruct", false, "I wonder what this one does...\n Example usage: .kb self_destruct"));
        put("flip", new Command("flip", false, "When you just can't handle someone anymore :V\n Example usage: .kb flip @User"));
        put("join_date", new Command("join_date", false, "This way you can know for how long you've been here!\n Example usage: .kb join_date   or   .kb join_date @user"));

        put("help", new Command("help", false, "Well this one is kind of easy to get"));
    }};

    private static MessageReceivedEvent MRE;
    private static GuildMessageReceivedEvent GMRE;
    private static PrivateMessageReceivedEvent PMRE;

    /**
     * Checks if the specified guild message is a command or not
     *
     * @param MRE the Guild Message Received Event
     * @return whether or not it is a command
     * @see #isCommand(MessageReceivedEvent)
     * @see #isCommand(PrivateMessageReceivedEvent)
     */
    public static boolean isCommand(GuildMessageReceivedEvent MRE) {
        boolean isCommand = false;
        for (int i = 0; i < Command.DEFAULT_ALIASES.length; i++) {
            if (raw(MRE).toLowerCase().startsWith(Command.DEFAULT_ALIASES[i])) {
                isCommand = commandList.containsKey(getCommand(raw(MRE)));      //gets raw(MRE), replaces the alias with nothing (aka removes it) and then splits whats left with regex " ", thus getting the first word after, aka the command
                break;
            }
        }
        return isCommand;
    }

    /**
     * Checks if the specified private message is a command or not
     *
     * @param MRE the Private Message Received Event
     * @return whether or not it is a command
     * @see #isCommand(GuildMessageReceivedEvent)
     * @see #isCommand(MessageReceivedEvent)
     */
    public static boolean isCommand(PrivateMessageReceivedEvent MRE) {
        boolean isCommand = false;
        for (int i = 0; i < Command.DEFAULT_ALIASES.length; i++) {
            if (raw(MRE).toLowerCase().startsWith(Command.DEFAULT_ALIASES[i])) {
                isCommand = commandList.containsKey(getCommand(raw(MRE)));      //gets raw(MRE), replaces the alias with nothing (aka removes it) and then splits whats left with regex " ", thus getting the first word after, aka the command
                break;
            }
        }
        return isCommand;
    }

    /**
     * Checks if the specified message is a command or not
     *
     * @param MRE the Message Received Event
     * @return whether or not it is a command
     * @see #isCommand(GuildMessageReceivedEvent)
     * @see #isCommand(PrivateMessageReceivedEvent)
     */
    public static boolean isCommand(MessageReceivedEvent MRE) {
        boolean isCommand = false;
        for (int i = 0; i < Command.DEFAULT_ALIASES.length; i++) {
            if (raw(MRE).toLowerCase().startsWith(Command.DEFAULT_ALIASES[i])) {
                isCommand = commandList.containsKey(getCommand(raw(MRE)));      //gets raw(MRE), replaces the alias with nothing (aka removes it) and then splits whats left with regex " ", thus getting the first word after, aka the command
                break;
            }
        }
        return isCommand;
    }

    /**
     * Checks if a command can be executed or not.
     * The command must be enabled, and be entered in
     * a valid Server and Text Channel, as well as
     * having been written by a valid user with a valid role.
     *
     * @param MRE     the Guild Message Received Event
     * @param command the command to be checked
     * @return whether or not the command can be executed
     * @see #canExecCommand(MessageReceivedEvent, Command)
     * @see #canExecCommand(PrivateMessageReceivedEvent, Command)
     */
    public static boolean canExecCommand(GuildMessageReceivedEvent MRE, Command command) {
        if (!command.enabled) return false;
        if (!Arrays.asList(command.allowedServersID).contains(MRE.getGuild().getIdLong()) && command.allowedServersID.length > 0)
            return false;
        if (!Arrays.asList(command.allowedTextChannelsID).contains(MRE.getChannel().getIdLong()) && command.allowedTextChannelsID.length > 0)
            return false;
        if (!Arrays.asList(command.userIDs).contains(MRE.getAuthor().getIdLong()) && command.userIDs.length > 0)
            return false;
        boolean validRole = false;
        for (int i = 0; i < MRE.getMember().getRoles().size(); i++) {
            if (Arrays.asList(command.roleIDs).contains(MRE.getMember().getRoles().toArray()[i])) {
                validRole = true;
            }
        }
        if (!validRole && command.roleIDs.length > 0) return false;
        return true;
    }

    /**
     * Checks if a command can be executed or not.
     * The command must be enabled, and be entered in
     * a valid Server and Text Channel, as well as
     * having been written by a valid user with a valid role.
     *
     * @param MRE     the Private Message Received Event
     * @param command the command to be checked
     * @return whether or not the command can be executed
     * @see #canExecCommand(GuildMessageReceivedEvent, Command)
     * @see #canExecCommand(MessageReceivedEvent, Command)
     */
    public static boolean canExecCommand(PrivateMessageReceivedEvent MRE, Command command) {
        if (!command.enabled) return false;
        if (!Arrays.asList(command.userIDs).contains(MRE.getAuthor().getIdLong()) && command.userIDs.length > 0)
            return false;
        return true;
    }

    /**
     * Checks if a command can be executed or not.
     * The command must be enabled, and be entered in
     * a valid Server and Text Channel, as well as
     * having been written by a valid user with a valid role.
     *
     * @param MRE     the Message Received Event
     * @param command the command to be checked
     * @return whether or not the command can be executed
     * @see #canExecCommand(GuildMessageReceivedEvent, Command)
     * @see #canExecCommand(PrivateMessageReceivedEvent, Command)
     */
    public static boolean canExecCommand(MessageReceivedEvent MRE, Command command) {
        if (!command.enabled) return false;
        if (!Arrays.asList(command.allowedServersID).contains(MRE.getGuild().getIdLong()) && command.allowedServersID.length > 0)
            return false;
        if (!Arrays.asList(command.allowedTextChannelsID).contains(MRE.getChannel().getIdLong()) && command.allowedTextChannelsID.length > 0)
            return false;
        if (!Arrays.asList(command.userIDs).contains(MRE.getAuthor().getIdLong()) && command.userIDs.length > 0)
            return false;
        boolean validRole = false;
        for (int i = 0; i < MRE.getMember().getRoles().size(); i++) {
            if (Arrays.asList(command.roleIDs).contains(MRE.getMember().getRoles().toArray()[i])) {
                validRole = true;
            }
        }
        if (!validRole && command.roleIDs.length > 0) return false;
        return true;
    }

    //first it lets us know what's going on via the console, then executes the command or lets the user know it cant be executed

    /**
     * Executes the specified command (it always assumes it is a command
     * since this method is only used after a isCommand check).
     * Detailed information about the command usage will be sent to the console.
     *
     * @param MRE the Message Received Event
     * @see #execCommand(GuildMessageReceivedEvent)
     * @see #execCommand(PrivateMessageReceivedEvent)
     */
    public static void execCommand(MessageReceivedEvent MRE) {
        System.out.println("Command: " + (raw(MRE)) + ", Guild: " + MRE.getGuild().getName() + ", Text Channel: " + MRE.getChannel().getName() + ", User: " + MRE.getAuthor().getName() + ", Can execute: " + canExecCommand(MRE, commandList.get(getCommand(raw(MRE)))) + ".");
        if (!canExecCommand(MRE, commandList.get(getCommand(raw(MRE))))) {
            MRE.getChannel().sendMessage("No can do!").queue();
            return;
        }
        switch (getCommand(raw(MRE))) {
            case "default" -> {
                Member self = MRE.getGuild().getSelfMember();
                self.modifyNickname("New Bot Name");
                File Avatar = new File("Location of profile picture");
                try {
                    self.getJDA().getSelfUser().getManager().setAvatar(Icon.from(Avatar)).complete();
                    MRE.getChannel().sendMessage("Bot has been set to default setings").queue();
                } catch (IOException e) {
                    MRE.getChannel().sendMessage("Failed to update avatar!! " + e).queue();
                }
            }
            case "nick" -> {
                MRE.getMessage().delete().queue();
                Member self = MRE.getGuild().getSelfMember();
                self.modifyNickname(getArgs(raw(MRE))).queue();
                MRE.getChannel().sendMessage("Nickname changed to " + getArgs(raw(MRE))).queue();
            }
            case "avatar" -> {
                MRE.getMessage().delete().queue();
                Member self = MRE.getGuild().getSelfMember();
                try {
                    MRE.getChannel().sendMessage("Avatar has been changed!");
                    self.getJDA().getSelfUser().getManager().setAvatar(Icon.from(
                            new URL(getArgs(raw(MRE))).openStream()
                    )).complete();
                } catch (IOException e) {
                    MRE.getChannel().sendMessage("Failed to update avatar!! " + e).queue();
                }
                MRE.getChannel().sendMessage("Avatar has been changed!").queue();
            }
            case "data" -> {
                MRE.getMessage().delete().queue();
                System.out.println(MRE.getChannel().getId());
                System.out.println("Roles:" + MRE.getGuild().getRoles().toString());
                //System.out.println(MRE.getGuild().getRoles().get(1));
                //System.out.println(MRE.getGuild().getRoles().size());
                System.out.println(MRE.getGuild().getMembersWithRoles(MRE.getGuild().getRoles().get(1)));
                System.out.println("Members:" + MRE.getGuild().getMembers().toString());
                System.out.print("Status: ");
                for (int i = 0; i < MRE.getGuild().getMembers().size(); i++) {
                    Member member = MRE.getGuild().getMembers().get(i);
                    System.out.print(member.getEffectiveName() + ": ");
                    if (member.getOnlineStatus().equals(OnlineStatus.OFFLINE)) {
                        System.out.print("off");
                    } else {
                        System.out.print(member.getOnlineStatus());
                    }
                    System.out.print("; ");
                }
            }
            case "lights" -> {
                if (Kebbot.Light != null) {
                    try {
                        String mes = YeeLight.getBaseProp(Kebbot.Light);
                        MRE.getChannel().sendMessage(mes).queue(message -> {
                            message.addReaction("\uD83D\uDCA1").queue(); //light bulb
                            message.addReaction("\uD83D\uDD34").queue(); //red circle
                            message.addReaction("\uD83D\uDD35").queue(); //blue circle
                            message.addReaction("\uD83D\uDFE2").queue(); //green circle
                            message.addReaction("⏰").queue(); //alarm clock
                            message.addReaction("➕").queue(); //plus
                            message.addReaction("➖").queue();
                        }); //minus
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            case "what_is" -> {
                try {
                    if ((Calculator.whatIs(getArgs(raw(MRE))) == Math.floor(Calculator.whatIs(getArgs(raw(MRE))))) && !Double.isInfinite(Calculator.whatIs(getArgs(raw(MRE))))) {
                        MRE.getChannel().sendMessage("That is " + (int) Calculator.whatIs(getArgs(raw(MRE))) + ", or at least it should be!").queue();
                    } else {
                        MRE.getChannel().sendMessage("That is " + Calculator.whatIs(getArgs(raw(MRE))) + ", or at least it should be!").queue();
                    }
                } catch (Exception e) {
                    MRE.getChannel().sendMessage("I can't do that one!").queue();
                }
            }
            case "meter" -> {
                try {
                    String target = getArgs(raw(MRE));
                    String[] args = target.split(" ");
                    if (args.length != 2) {
                        throw new Exception("There should only be the meter and the target!");
                    }
                    int percentage = ((args[0] + args[1]).hashCode() % 100);
                    if (percentage < 0) percentage *= -1;
                    MRE.getChannel().sendMessage(args[1] + " is " + percentage + "% " + args[0] + "!").queue();
                } catch (Exception e) {
                    MRE.getChannel().sendMessage("That command is invalid, please check the spelling and try again!").queue();
                }
            }
            case "self_destruct" -> {
                //say explosion and delete message
                MRE.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
                MRE.getChannel().sendMessage("This will be deleted after 5 seconds").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            }
            case "flip" -> {
                String target = getArgs(raw(MRE));
                System.out.println(target);
                if (target.contains("Kebbot") || target.equals("<@!774809328854499388>") || target.equals("<@!286696325310054400>")) {
                    MRE.getChannel().sendMessage("You can't flip me <@!" + MRE.getAuthor().getId() + "> but guess what, Fuck You ╭∩╮(・◡・)╭∩╮").queue();
                } else {
                    MRE.getChannel().sendMessage("Dear " + target + ", Fuck You ╭∩╮(・◡・)╭∩╮").queue();
                }
            }
            case "help" -> {
                String out = "";
                //admin gets to see all the commands
                if (MRE.getAuthor().getId().toString().equals(Command.ADMIN_ID.toString())) {
                    for (Command command : commandList.values()) {
                        if (command.userIDs.length == 1 && Arrays.stream(command.userIDs).anyMatch(Command.ADMIN_ID::equals)) {
                            out += "ADMIN Command: " + command.name + " ---> " + command.description + "\n\n";
                        } else {
                            out += "Command: " + command.name + " ---> " + command.description + "\n\n";
                        }
                    }
                    //other people only get to see the rest of the commands
                } else {
                    for (Command command : commandList.values()) {
                        //check if its an admin command
                        if (command.userIDs.length == 1 && Arrays.stream(command.userIDs).anyMatch(Command.ADMIN_ID::equals)) {
                            continue;
                        }
                        out += "Command: " + command.name + " ---> " + command.description + "\n\n";
                    }
                }
                MRE.getChannel().sendMessage(out).queue();
            }

            default -> System.out.println("The command is either Private or Guild exclusive");
        }
    }

    /**
     * Executes the specified command (it always assumes it is a command
     * since this method is only used after a isCommand check).
     * Detailed information about the command usage will be sent to the console.
     *
     * @param MRE the Private Message Received Event
     * @see #execCommand(MessageReceivedEvent)
     * @see #execCommand(GuildMessageReceivedEvent)
     */
    public static void execCommand(PrivateMessageReceivedEvent MRE) {

    }

    /**
     * Executes the specified command (it always assumes it is a command
     * since this method is only used after a isCommand check).
     * Detailed information about the command usage will be sent to the console.
     *
     * @param MRE the Guild Message Received Event
     * @see #execCommand(MessageReceivedEvent)
     * @see #execCommand(PrivateMessageReceivedEvent)
     */
    public static void execCommand(GuildMessageReceivedEvent MRE) {
        if (!canExecCommand(MRE, commandList.get(getCommand(raw(MRE))))) {
            MRE.getChannel().sendMessage("no can do").queue();
            return;
        }
        switch (getCommand(raw(MRE))) {
            case "join_date" -> {
                System.out.println(hasArgs(raw(MRE)));
                if (!hasArgs(raw(MRE))) {
                    MRE.getChannel().sendMessage("You joined this server on: " + FORMATTER.format(MRE.getMember().getTimeJoined())).queue();
                } else {
                    String target = getArgs(raw(MRE));
                    MRE.getChannel().sendMessage(target + " joined this server on: " + FORMATTER.format(MRE.getGuild().getMemberById(stringID(target)).getTimeJoined())).queue();
                }
            }
            case "ping" -> {
                MessageChannel channel = MRE.getChannel();
                long time = System.currentTimeMillis();
                channel.sendMessage("Pong!") /* => RestAction<Message> */
                        .queue(response /* => Message */ -> response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue());
            }
            default -> System.out.println("This command was probably Private or General");
        }
    }

    /**
     * Executes an action if a reaction has been added to a message.
     * This is currently used for controlling a Yeelight Device.
     *
     * @param GMRAE the Guild Message Reaction Add Event
     * @see #onReactionRemove(GuildMessageReactionRemoveEvent)
     * @see YeeLight
     */
    public static void onReactionAdd(GuildMessageReactionAddEvent GMRAE) {
        String[] firstWords = GMRAE.getChannel().retrieveMessageById(GMRAE.getMessageId()).complete().getContentRaw().split(" ", 4);
        String raw = firstWords[0] + " " + firstWords[1] + " " + firstWords[2];
        switch (raw) {
            // Complete message would be: "Use the reactions bellow to control the lights!", but it will be edited a lot of times
            case "Use the reactions" -> {
                if (Kebbot.Light == null || GMRAE.getMember().equals(GMRAE.getGuild().getSelfMember())) return;
                if (GMRAE.getChannel().retrieveMessageById(GMRAE.getMessageId()).complete().getMember().equals(GMRAE.getGuild().getSelfMember())) {
                    switch (GMRAE.getReactionEmote().getEmoji()) {
                        case "\uD83D\uDCA1" -> {
                            try {
                                Kebbot.Light.setPower(true);
                                GMRAE.getChannel().retrieveMessageById(GMRAE.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light)).queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        case "\uD83D\uDD34" -> {
                            try {
                                Kebbot.Light.setRGB(255, 0, 0);
                                GMRAE.getChannel().retrieveMessageById(GMRAE.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light)).queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        case "\uD83D\uDD35" -> {
                            try {
                                Kebbot.Light.setRGB(0, 0, 255);
                                GMRAE.getChannel().retrieveMessageById(GMRAE.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light)).queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        case "\uD83D\uDFE2" -> {
                            try {
                                Kebbot.Light.setRGB(0, 255, 0);
                                GMRAE.getChannel().retrieveMessageById(GMRAE.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light)).queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        case "⏰" -> {
                            try {
                                Kebbot.Light.toggle();
                                Thread.sleep(600);
                                Kebbot.Light.toggle();
                                Thread.sleep(600);
                                Kebbot.Light.toggle();
                                Thread.sleep(600);
                                Kebbot.Light.toggle();
                                GMRAE.getChannel().retrieveMessageById(GMRAE.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light) + "  (Owner has been notified)").queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        case "➕" -> {
                            try {
                                int br = Integer.parseInt(Kebbot.Light.getProperties().get(YeelightProperty.BRIGHTNESS));
                                Kebbot.Light.setBrightness(br + 10);
                                GMRAE.getChannel().retrieveMessageById(GMRAE.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light) + "  (Added 10% Brightness)").queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        case "➖" -> {
                            try {
                                int br = Integer.parseInt(Kebbot.Light.getProperties().get(YeelightProperty.BRIGHTNESS));
                                Kebbot.Light.setBrightness(br - 10);
                                GMRAE.getChannel().retrieveMessageById(GMRAE.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light) + "  (Removed 10% Brightness)").queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            default -> {
                return;
            }
        }
    }

    /**
     * Executes an action if a reaction has been removed from a message.
     * This is currently used for controlling a Yeelight Device.
     *
     * @param GMRRE the Guild Message Reaction Remove Event
     * @see #onReactionAdd(GuildMessageReactionAddEvent)
     * @see YeeLight
     */
    public static void onReactionRemove(GuildMessageReactionRemoveEvent GMRRE) {
        String[] firstWords = GMRRE.getChannel().retrieveMessageById(GMRRE.getMessageId()).complete().getContentRaw().split(" ", 4);
        String raw = firstWords[0] + " " + firstWords[1] + " " + firstWords[2];
        switch (raw) {
            case "Use the reactions" -> {
                if (Kebbot.Light == null || GMRRE.getMember().equals(GMRRE.getGuild().getSelfMember())) return;
                if (GMRRE.getChannel().retrieveMessageById(GMRRE.getMessageId()).complete().getMember().equals(GMRRE.getGuild().getSelfMember())) {
                    switch (GMRRE.getReactionEmote().getEmoji()) {
                        case "\uD83D\uDCA1" -> {
                            try {
                                Kebbot.Light.setPower(false);
                                GMRRE.getChannel().retrieveMessageById(GMRRE.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light)).queue();
                            } catch (Exception e) {
                                MRE.getChannel().sendMessage(e.toString()).queue();
                                return;
                            }
                        }
                    }
                }
            }
            default -> {
                return;
            }
        }
    }

    /**
     * Gets the command from a string (the first position
     * of the array should be the alias, and the second one
     * the command, the remaining ones the arguments for said command.
     * The lower case is so the command isn't case sensitive)
     *
     * @param input the input string (from the user)
     * @return the command name
     */
    private static String getCommand(String input) {
        return input.split(" ")[1].toLowerCase();
    }

    /**
     * Gets the command from a string (the first position
     * of the array should be the alias, and the second one
     * the command, the remaining ones (in this case just one
     * as we set the limit to 3) the arguments for said command.
     * The lower case is so the command isn't case sensitive)
     *
     * @param input the input string (from the user)
     * @return the command arguments
     */
    private static String getArgs(String input) {
        return input.split(" ", 3)[2];
    }

    /**
     * Checks if a given user input has arguments
     * (information after command name)
     *
     * @param input the input string (from the user)
     * @return whether or not it has arguments
     */
    private static boolean hasArgs(String input) {
        boolean hasArgs = true;
        if (input.split(" ").length < 3) hasArgs = false;
        return hasArgs;
    }

    /**
     * Gets the raw content of a user message
     * (converts it into a usable string)
     *
     * @param MRE the Message Received Event
     * @return the user input has a String
     * @see #raw(GuildMessageReceivedEvent)
     * @see #raw(PrivateMessageReceivedEvent)
     */
    public static String raw(MessageReceivedEvent MRE) {
        return MRE.getMessage().getContentRaw();
    }

    /**
     * Gets the raw content of a user message
     * (converts it into a usable string)
     *
     * @param MRE the Guild Message Received Event
     * @return the user input has a String
     * @see #raw(MessageReceivedEvent)
     * @see #raw(PrivateMessageReceivedEvent)
     */
    public static String raw(GuildMessageReceivedEvent MRE) {
        return MRE.getMessage().getContentRaw();
    }

    /**
     * Gets the raw content of a user message
     * (converts it into a usable string)
     *
     * @param MRE the Private Message Received Event
     * @return the user input has a String
     * @see #raw(MessageReceivedEvent)
     * @see #raw(GuildMessageReceivedEvent)
     */
    public static String raw(PrivateMessageReceivedEvent MRE) {
        return MRE.getMessage().getContentRaw();
    }

    /**
     * Transforms the formatted discord id (when reading
     * raw content from a message, if the user uses @other_user
     * then the raw content will be formatted) into
     * a readable and number only ID
     *
     * @param id the user's id formatted by discord
     * @return the user's id as a usable Long
     */
    public static Long stringID(String id) {
        if (id.length() == 22) {
            return Long.parseLong(id.substring(3, 21));
        } else {
            return (null);
        }
    }
}
