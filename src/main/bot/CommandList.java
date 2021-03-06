package bot;

import casino.BlackJack;
import casino.Currency;
import casino.EuroMillions;
import chess.*;
import codeWords.*;
import net.dv8tion.jda.api.entities.*;
import stockFish.Stockfish;
import net.dv8tion.jda.api.EmbedBuilder;
import utils.Calculator;
import utils.YeeLight;
import com.mollin.yapi.enumeration.YeelightProperty;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import websiteScrapper.other.Container;
import websiteScrapper.other.MAL;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
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
 * @version 1.2
 * @see Kebbot
 * @see Command
 * @see net.dv8tion.jda.api.JDA
 */
public class CommandList {
    private static ArrayList<Container> animeList = new ArrayList<>();
    private static GamesContainer container = Kebbot.container;
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
    private static LinkedHashMap<String, Command> commandList = new LinkedHashMap<>() {{
        //first are all the admin/owner commands
        put("default", new Command("default", true, "Changes the profile picture of the bot to the default one\nExample Usage: .kb default"));
        put("nick", new Command("nick", true, "Changes the nickname of the bot to a different one\nExample Usage: .kb nick new_nick"));
        put("avatar", new Command("avatar", true, "Changes the avatar of the bot to a different one\nExample Usage: .kb avatar URL_of_Avatar"));
        put("data", new Command("data", true, "Gets some data from the server so the bot can work better :D\nExample Usage: .kb data"));
        put("search", new Command("search", true, "Searches the current channel for the given parameter  -  Example Usage: .kb search keyword one and something else"));
        put("ping", new Command("ping", true, "Pings the bot, getting the response in ms\nExample Usage: .kb ping"));
        put("lights", new Command("lights", true, "Gives some options to control lights (Yeelight)\nExample Usage: .kb lights"));
        put("set_wallet", new Command("set_wallet", true, "Sets a player's wallet value  -  Example Usage: .kb set_wallet 1000"));
        //these are the other commands, reminder that the constructor for the command class is: (Long[] allowedServersID, Long[] allowedTextChannelsID, Long[] roleIDs, Long[] userIDs, String[] aliases, String name, boolean enabled, boolean canDisable)
        put("chess", new Command("chess", false, "Challenge someone (or the Computer) to a Chess game!\nExample Usage: .kb chess @User   or   .kb chess CPU"));
        put("cw_start", new Command("cw_start", false, "Starts a CodeWords game!\nExample Usage: .kb cw_start"));
        put("chess_help", new Command("chess_help", false, "Gives you more information about the Chess Game!\nExample Usage: .kb chess_help"));
        put("cw_help", new Command("cw_help", false, "Gives you more information about the CodeWords Game!\nExample Usage: .kb chess_help"));
        put("what_is", new Command("what_is", false, "Basic calculator  -   Example usage: .kb what_is 5 / 2  (Use: + - * / ^ )"));
        put("meter", new Command("meter", false, "Measure how much of something someone is!\n Example usage: .kb meter Happy @User"));
        put("self_destruct", new Command("self_destruct", false, "I wonder what this one does...\n Example usage: .kb self_destruct"));
        put("flip", new Command("flip", false, "When you just can't handle someone anymore :V\n Example usage: .kb flip @User"));
        put("join_date", new Command("join_date", false, "This way you can know for how long you've been here!\n Example usage: .kb join_date   or   .kb join_date @user"));
        put("mal", new Command("mal", false, "Collection of commands for searching the My Anime List website! (currently under construction)  -   Use the command mal_help for example usages"));
        put("mal_help", new Command("mal_help", false, "Lists all possible arguments for mal command (currently under construction)  -   Usage: .kb mal_help"));
        put("blackjack", new Command("blackjack", false, "Starts a new BlackJack game, with the given wager! (currently under construction)  -   Usage: .kb blackjack 500"));
        put("coin_flip", new Command("coin_flip", false, "Starts a new Coin flip game, with the given wager!  -   Usage: .kb coin_flip 500 heads"));
        put("euro_millions", new Command("euro_millions", false, "Play the euro millions game, and test your luck! (Wagers 2.50???)!  -   Usage: .kb euro_millions 13 26 47 33 50 1 2   or   .kb euro_millions 13 26 47 33 50 1 2 multi 20"));
        put("wallet", new Command("wallet", false, "Gets the amount of money on your wallet (Use it in the Bot's features)!  -   Usage: .kb wallet"));
        put("help", new Command("help", false, "Well this one is kind of easy to get"));
    }};
    
       /**
     * Checks if the specified guild message is a command or not
     *
     * @param mre the Guild Message Received Event
     * @return whether or not it is a command
     * @see #isCommand(MessageReceivedEvent)
     * @see #isCommand(PrivateMessageReceivedEvent)
     */
    public static boolean isCommand(GuildMessageReceivedEvent mre) {
        boolean isCommand = false;
        for (int i = 0; i < Command.DEFAULT_ALIASES.length; i++) {
            if (raw(mre).toLowerCase().startsWith(Command.DEFAULT_ALIASES[i])) {
                isCommand = commandList.containsKey(getCommand(raw(mre)));      //gets raw(mre), replaces the alias with nothing (aka removes it) and then splits whats left with regex " ", thus getting the first word after, aka the command
                break;
            }
        }
        return isCommand;
    }

    /**
     * Checks if the specified private message is a command or not
     *
     * @param mre the Private Message Received Event
     * @return whether or not it is a command
     * @see #isCommand(GuildMessageReceivedEvent)
     * @see #isCommand(MessageReceivedEvent)
     */
    public static boolean isCommand(PrivateMessageReceivedEvent mre) {
        boolean isCommand = false;
        for (int i = 0; i < Command.DEFAULT_ALIASES.length; i++) {
            if (raw(mre).toLowerCase().startsWith(Command.DEFAULT_ALIASES[i])) {
                isCommand = commandList.containsKey(getCommand(raw(mre)));      //gets raw(mre), replaces the alias with nothing (aka removes it) and then splits whats left with regex " ", thus getting the first word after, aka the command
                break;
            }
        }
        return isCommand;
    }

    /**
     * Checks if the specified message is a command or not
     *
     * @param mre the Message Received Event
     * @return whether or not it is a command
     * @see #isCommand(GuildMessageReceivedEvent)
     * @see #isCommand(PrivateMessageReceivedEvent)
     */
    public static boolean isCommand(MessageReceivedEvent mre) {
        boolean isCommand = false;
        for (int i = 0; i < Command.DEFAULT_ALIASES.length; i++) {
            if (raw(mre).toLowerCase().startsWith(Command.DEFAULT_ALIASES[i])) {
                isCommand = commandList.containsKey(getCommand(raw(mre)));      //gets raw(MRE), replaces the alias with nothing (aka removes it) and then splits whats left with regex " ", thus getting the first word after, aka the command
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
     * @param mre     the Guild Message Received Event
     * @param command the command to be checked
     * @return whether or not the command can be executed
     * @see #canExecCommand(MessageReceivedEvent, Command)
     * @see #canExecCommand(PrivateMessageReceivedEvent, Command)
     */
    public static boolean canExecCommand(GuildMessageReceivedEvent mre, Command command) {
        if (!command.enabled) return false;
        if (!Arrays.asList(command.allowedServersID).contains(mre.getGuild().getIdLong()) && command.allowedServersID.length > 0)
            return false;
        if (!Arrays.asList(command.allowedTextChannelsID).contains(mre.getChannel().getIdLong()) && command.allowedTextChannelsID.length > 0)
            return false;
        if (!Arrays.asList(command.userIDs).contains(mre.getAuthor().getIdLong()) && command.userIDs.length > 0)
            return false;
        boolean validRole = false;
        for (int i = 0; i < mre.getMember().getRoles().size(); i++) {
            if (Arrays.asList(command.roleIDs).contains(mre.getMember().getRoles().toArray()[i])) {
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
     * @param mre     the Private Message Received Event
     * @param command the command to be checked
     * @return whether or not the command can be executed
     * @see #canExecCommand(GuildMessageReceivedEvent, Command)
     * @see #canExecCommand(MessageReceivedEvent, Command)
     */
    public static boolean canExecCommand(PrivateMessageReceivedEvent mre, Command command) {
        if (!command.enabled) return false;
        if (!Arrays.asList(command.userIDs).contains(mre.getAuthor().getIdLong()) && command.userIDs.length > 0)
            return false;
        return true;
    }

    /**
     * Checks if a command can be executed or not.
     * The command must be enabled, and be entered in
     * a valid Server and Text Channel, as well as
     * having been written by a valid user with a valid role.
     *
     * @param mre     the Message Received Event
     * @param command the command to be checked
     * @return whether or not the command can be executed
     * @see #canExecCommand(GuildMessageReceivedEvent, Command)
     * @see #canExecCommand(PrivateMessageReceivedEvent, Command)
     */
    public static boolean canExecCommand(MessageReceivedEvent mre, Command command) {
        if (!command.enabled) return false;
        if (!Arrays.asList(command.allowedServersID).contains(mre.getGuild().getIdLong()) && command.allowedServersID.length > 0)
            return false;
        if (!Arrays.asList(command.allowedTextChannelsID).contains(mre.getChannel().getIdLong()) && command.allowedTextChannelsID.length > 0)
            return false;
        if (!Arrays.asList(command.userIDs).contains(mre.getAuthor().getIdLong()) && command.userIDs.length > 0)
            return false;
        boolean validRole = false;
        for (int i = 0; i < mre.getMember().getRoles().size(); i++) {
            if (Arrays.asList(command.roleIDs).contains(mre.getMember().getRoles().toArray()[i])) {
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
     * @param mre the Message Received Event
     * @see #execCommand(GuildMessageReceivedEvent)
     * @see #execCommand(PrivateMessageReceivedEvent)
     */
    public static void execCommand(MessageReceivedEvent mre) {
        System.out.println("Command: " + (raw(mre)) + ", Guild: " + mre.getGuild().getName() + ", Text Channel: " + mre.getChannel().getName() + ", User: " + mre.getAuthor().getName() + ", Can execute: " + canExecCommand(mre, commandList.get(getCommand(raw(mre)))) + ".");
        if (!canExecCommand(mre, commandList.get(getCommand(raw(mre))))) {
            mre.getChannel().sendMessage("No can do!").queue();
            return;
        }
        switch (getCommand(raw(mre))) {
            case "default" -> {
                Member self = mre.getGuild().getSelfMember();
                 self.modifyNickname("New Bot Name");
                File Avatar = new File("Location of profile picture");
                try {
                    self.getJDA().getSelfUser().getManager().setAvatar(Icon.from(Avatar)).complete();
                    mre.getChannel().sendMessage("Bot has been set to default setings").queue();
                } catch (IOException e) {
                    mre.getChannel().sendMessage("Failed to update avatar!! " + e).queue();
                }
            }
            case "nick" -> {
                mre.getMessage().delete().queue();
                Member self = mre.getGuild().getSelfMember();
                self.modifyNickname(getArgs(raw(mre))).queue();
                mre.getChannel().sendMessage("Nickname changed to " + getArgs(raw(mre))).queue();
            }
            case "avatar" -> {
                mre.getMessage().delete().queue();
                Member self = mre.getGuild().getSelfMember();
                try {
                    mre.getChannel().sendMessage("Avatar has been changed!");
                    self.getJDA().getSelfUser().getManager().setAvatar(Icon.from(
                            new URL(getArgs(raw(mre))).openStream()
                    )).complete();
                } catch (IOException e) {
                    mre.getChannel().sendMessage("Failed to update avatar!! " + e).queue();
                }
                mre.getChannel().sendMessage("Avatar has been changed!").queue();
            }
            case "data" -> {
                mre.getMessage().delete().queue();
                System.out.println(mre.getChannel().getId());
                System.out.println("Roles:" + mre.getGuild().getRoles().toString());
                //System.out.println(MRE.getGuild().getRoles().get(1));
                //System.out.println(MRE.getGuild().getRoles().size());
                System.out.println(mre.getGuild().getMembersWithRoles(mre.getGuild().getRoles().get(1)));
                System.out.println("Members:" + mre.getGuild().getMembers().toString());
                System.out.print("Status: ");
                for (int i = 0; i < mre.getGuild().getMembers().size(); i++) {
                    Member member = mre.getGuild().getMembers().get(i);
                    System.out.print(member.getEffectiveName() + ": ");
                    if (member.getOnlineStatus().equals(OnlineStatus.OFFLINE)) {
                        System.out.print("off");
                    } else {
                        System.out.print(member.getOnlineStatus());
                    }
                    System.out.print("; ");
                }
            }
            case "search" -> {
                ArrayList<String> search = search(getArgs(raw(mre)),mre);
                StringBuilder sb = new StringBuilder();
                for(String string:search) {
                    sb.append(string);
                    sb.append("\n");
                }
                String out = sb.toString();
                ArrayList<String> output = new ArrayList<>();
                while(out.length()>2000) {
                    output.add(out.substring(0,1999));
                    out=out.substring(2000,out.length()-1);
                }
                output.add(out);
                for(String string:output) {
                    mre.getChannel().sendMessage(string).queue();
                }
            }    
            case "lights" -> {
                if (Kebbot.Light != null) {
                    try {
                        String mes = YeeLight.getBaseProp(Kebbot.Light);
                        mre.getChannel().sendMessage(mes).queue(message -> {
                            message.addReaction("\uD83D\uDCA1").queue(); //light bulb
                            message.addReaction("\uD83D\uDD34").queue(); //red circle
                            message.addReaction("\uD83D\uDD35").queue(); //blue circle
                            message.addReaction("\uD83D\uDFE2").queue(); //green circle
                            message.addReaction("???").queue(); //alarm clock
                            message.addReaction("???").queue(); //plus
                            message.addReaction("???").queue();
                        }); //minus
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            case "set_wallet" -> {
                String[] args = getArgs(raw(mre)).split(" ");
                if(args.length!=2)return;
                String stringID=args[0];
                Long id=null;
                double value= Integer.MIN_VALUE;
                try{
                    stringID=stringID.replace("<@!","");
                    stringID=stringID.replace(">","");
                    id=Long.parseLong(stringID);
                    value=Double.parseDouble(args[1]);
                } catch (Exception ignored) {return;}
                if(id!=null && value!=Integer.MIN_VALUE) {
                    Currency.setCurrency(id,value);
                    mre.getChannel().sendMessage("Changed amount to: "+value+"???").queue();
                }
            }    
            case "what_is" -> {
                try {
                    if ((Calculator.whatIs(getArgs(raw(mre))) == Math.floor(Calculator.whatIs(getArgs(raw(mre))))) && !Double.isInfinite(Calculator.whatIs(getArgs(raw(mre))))) {
                        mre.getChannel().sendMessage("That is " + (int) Calculator.whatIs(getArgs(raw(mre))) + ", or at least it should be!").queue();
                    } else {
                        mre.getChannel().sendMessage("That is " + Calculator.whatIs(getArgs(raw(mre))) + ", or at least it should be!").queue();
                    }
                } catch (Exception e) {
                    mre.getChannel().sendMessage("I can't do that one!").queue();
                }
            }
            case "meter" -> {
                try {
                    String target = getArgs(raw(mre));
                    String[] args = target.split(" ");
                    if (args.length != 2) {
                        throw new Exception("There should only be the meter and the target!");
                    }
                    int percentage = ((args[0] + args[1]).hashCode() % 100);
                    if (percentage < 0) percentage *= -1;
                    mre.getChannel().sendMessage(args[1] + " is " + percentage + "% " + args[0] + "!").queue();
                } catch (Exception e) {
                    mre.getChannel().sendMessage("That command is invalid, please check the spelling and try again!").queue();
                }
            }
            case "self_destruct" -> {
                //say explosion and delete message
                mre.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
                mre.getChannel().sendMessage("This will be deleted after 5 seconds").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            }
            case "flip" -> {
                String target = getArgs(raw(mre));
                if (target.contains("Kebbot") || target.equals("Owner ID") || target.equals("Bot ID")) {
                    mre.getChannel().sendMessage("You can't flip me <@!" + mre.getAuthor().getId() + "> but guess what, Fuck You ?????????(?????????)?????????").queue();
                } else {
                    mre.getChannel().sendMessage("Dear " + target + ", Fuck You ?????????(?????????)?????????").queue();
                }
            }
            case "mal" -> {
                String[] args = getArgs(raw(mre)).split(" ");
                double params1 = -1;
                if(animeList.isEmpty()) {
                    animeList = MAL.animeInfo(true);
                }
                ArrayList<Container> content;
                if(args.length==2) {
                    try{
                        params1 = Double.parseDouble(args[1]);
                    } catch (Exception e) {
                        mre.getChannel().sendMessage("Invalid parameters").queue();
                        return;
                    }
                    String season;
                    switch (args[0]) {
                        case "spring"-> {season="spring";}
                        case "summer"-> {season="summer";}
                        case "winter"-> {season="winter";}
                        case "fall"-> {season="fall";}
                        default -> {return;}
                    }
                    if(params1<1920||params1>2021) return;
                    content=MAL.animeInfo(season,(int)params1,true);
                }
            }
            case "blackjack" -> {
                String[] args = getArgs(raw(mre)).split(" ");
                if(args.length!=1)return;
                if(container.getBJGameID(mre.getAuthor().getIdLong())!=-1) return;
                int wager = Integer.MIN_VALUE;
                try {
                    wager = Integer.parseInt(args[0]);
                } catch (Exception ignored) {return;}
                if(wager== Integer.MIN_VALUE) return;
                if(Currency.getCurrency(mre.getAuthor().getIdLong())<wager) {
                    mre.getChannel().sendMessage("You can't bet that much, please add more money or wager a smaller value.\n(Your current currency is: "+ Currency.getCurrency(mre.getAuthor().getIdLong()) +"???)").queue();
                    return;
                }
                int id=container.newBJ(mre.getAuthor().getIdLong(),wager);
                BlackJack game = container.getBJGame(id);
                game.setMessageChannel(mre.getChannel());
                mre.getChannel().sendMessage("You have started a new BlackJack game!   Game ID: "+id).queue();
                mre.getChannel().sendMessage("This is the current state of the game:\n"+game.toString()).queue();
                String firstOver = game.firstOver();
                if(!firstOver.equals("")) mre.getChannel().sendMessage(firstOver).queue();
            }
            case "coin_flip" -> {
                String[] args = getArgs(raw(mre)).split(" ");
                if(args.length!=2)return;
                if(container.getBJGameID(mre.getAuthor().getIdLong())!=-1) return;
                int wager = Integer.MIN_VALUE;
                String sideString = args[1].toLowerCase();
                Boolean side = null;
                try {
                    wager = Integer.parseInt(args[0]);
                    if(sideString.equals("h")||sideString.equals("heads")||sideString.equals("1")||sideString.equals("true")) {
                        side = true;
                    }
                    if(sideString.equals("t")||sideString.equals("tails")||sideString.equals("0")||sideString.equals("false")) {
                        side = false;
                    }
                    if(side==null) return;
                } catch (Exception ignored) {return;}
                if(wager == Integer.MIN_VALUE) return;
                if(Currency.getCurrency(mre.getAuthor().getIdLong())<wager) {
                    mre.getChannel().sendMessage("You can't bet that much, please add more money or wager a smaller value.\n(Your current currency is: "+ Currency.getCurrency(mre.getAuthor().getIdLong()) +"???)").queue();
                    return;
                }
                Random random = new Random();
                boolean flip = random.nextBoolean();
                String coinSide = flip ? "heads":"tails";
                if(flip==side) {
                    Currency.addCurrency(mre.getAuthor().getIdLong(),wager);
                    mre.getChannel().sendMessage("You guessed correctly, the coin landed on "+ coinSide +"!").queue();
                } else {
                    Currency.removeCurrency(mre.getAuthor().getIdLong(),wager);
                    mre.getChannel().sendMessage("You guessed incorrectly, the coin landed on "+ coinSide +"...").queue();
                }
            }
            case "euro_millions" -> {
                String[] args = getArgs(raw(mre)).split(" ");
                if(args.length!=7&&args.length!=9) return;
                if(args.length==9&&!args[7].toLowerCase().equals("multi")) return;
                int[] guess = new int[7];
                int multi = -1;
                try {
                    for(int i=0;i<7;i++) {
                        guess[i]=Integer.parseInt(args[i]);
                    }
                    if(args.length==9) multi=Integer.parseInt(args[8]);
                } catch (Exception ignored) { }
                if(args.length==9) {
                    if(multi<2) return;
                    double total = 0;
                    for(int i=0;i<multi;i++) {
                        Currency.removeCurrency(mre.getAuthor().getIdLong(),2.50);
                        EuroMillions game = new EuroMillions();
                        total += game.hasPrize(guess);
                    }
                    if(total>0) {
                        mre.getChannel().sendMessage("You won "+total+"??? in "+multi+" attempts, congratulations!").queue();
                    } else {
                        mre.getChannel().sendMessage("You didn't win anything, even on "+multi+" attempts, better luck next time!").queue();
                    }
                } else {
                    if(EuroMillions.validGuess(guess)) {
                        Currency.removeCurrency(mre.getAuthor().getIdLong(),2.50);
                        EuroMillions game = new EuroMillions();
                        double prize = game.hasPrize(guess);
                        mre.getChannel().sendMessage("The lottery is over: "+game.toString()).queue();
                        if(prize>0) {
                            mre.getChannel().sendMessage("You won "+prize+"???, congratulations!").queue();
                        } else {
                            mre.getChannel().sendMessage("You didn't win anything, better luck next time!").queue();
                        }
                    }
                }
            }
            case "wallet" -> {
                double currency = Currency.getCurrency(mre.getAuthor().getIdLong());
                mre.getChannel().sendMessage("You have "+currency+"??? on your wallet!").queue();
            }    
            case "help" -> {
                ArrayList<String> output = new ArrayList<>();
                //admin gets to see all the commands
                if (mre.getAuthor().getId().toString().equals(Command.ADMIN_ID.toString())) {
                    for (Command command : commandList.values()) {
                        if (command.userIDs.length == 1 && Arrays.asList(command.userIDs).contains(Command.ADMIN_ID)) {
                            output.add("**ADMIN - " + command.name + "** : " + command.description + "\n");
                        } else {
                            output.add("**" + command.name + "** : " + command.description + "\n");
                        } } }
                //other people only get to see the rest of the commands
                else {
                    for (Command command : commandList.values()) {
                        //check if its an admin command
                        if (command.userIDs.length == 1 && Arrays.asList(command.userIDs).contains(Command.ADMIN_ID)) continue;
                        output.add("**" + command.name + "** : " + command.description + "\n");
                    } }
                //splits the message to avoid the 2000 char limit
                while(output.size()>10) {
                    StringBuilder out = new StringBuilder();
                    for(int i=0;i<10;i++) {
                        out.append(output.get(0));
                        output.remove(0);
                    }
                    mre.getChannel().sendMessage(out.toString()).queue();
                }
                StringBuilder out = new StringBuilder();
                for(int i=0;i<output.size();i++) {
                    out.append(output.get(i));
                }
                mre.getChannel().sendMessage(out.toString()).queue();
            }
            default -> System.out.println("The command is either Private or Guild exclusive");
        }
    }

    /**
     * Executes the specified command (it always assumes it is a command
     * since this method is only used after a isCommand check).
     * Detailed information about the command usage will be sent to the console.
     *
     * @param mre the Private Message Received Event
     * @see #execCommand(MessageReceivedEvent)
     * @see #execCommand(GuildMessageReceivedEvent)
     */
    public static void execCommand(PrivateMessageReceivedEvent mre) {

    }

    /**
     * Executes the specified command (it always assumes it is a command
     * since this method is only used after a isCommand check).
     * Detailed information about the command usage will be sent to the console.
     *
     * @param mre the Guild Message Received Event
     * @see #execCommand(MessageReceivedEvent)
     * @see #execCommand(PrivateMessageReceivedEvent)
     */
    public static void execCommand(GuildMessageReceivedEvent mre) {
        if (!canExecCommand(mre, commandList.get(getCommand(raw(mre))))) {
            mre.getChannel().sendMessage("no can do").queue();
            return;
        }
        switch (getCommand(raw(mre))) {
            case "join_date" -> {
                if (!hasArgs(raw(mre))) {
                    mre.getChannel().sendMessage("You joined this server on: " + FORMATTER.format(mre.getMember().getTimeJoined())).queue();
                } else {
                    String target = getArgs(raw(mre));
                    mre.getChannel().sendMessage(target + " joined this server on: " + FORMATTER.format(mre.getGuild().getMemberById(idAsLong(target)).getTimeJoined())).queue();
                }
            }
            case "ping" -> {
                MessageChannel channel = mre.getChannel();
                long time = System.currentTimeMillis();
                channel.sendMessage("Pong!") /* => RestAction<Message> */
                        .queue(response /* => Message */ -> response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue());
            }
            case "cw_start" -> {
                if(container.getCWGameID(mre.getAuthor().getIdLong())!=-1) return;
                int id=container.newCW();
                container.getCWGame(id).setTextChannel(mre.getChannel());
                mre.getChannel().sendMessage("CodeWords: Please click on the Plus sign below to join!   Game ID: "+id+"").queue(message -> {
                    message.addReaction("\uD83D\uDFE2").queue(); //green circle
                    message.addReaction("???").queue(); //plus
                });
            }
            case "chess" -> {
                if(container.getChessGameID(mre.getAuthor().getIdLong())!=-1) return;
                String args = getArgs(raw(mre));
                String[] split = args.split(" ");
                if(split.length!=1&&split.length!=2) return;
                int id=-1;
                boolean def = true;
                if(split.length==1) {
                    if(args.toLowerCase().equals("cpu")) {
                        boolean random = Chess.randomizeSide();
                        Player p1 = new Player(random, true, mre.getAuthor().getIdLong());
                        id = container.newChess(p1);
                    } else {
                        if(!isInGuild(idAsLong(args),mre) ) return;
                        boolean random = Chess.randomizeSide();
                        Player p1 = new Player(random, true, mre.getAuthor().getIdLong());
                        Player p2 = new Player(random, true, idAsLong(args));
                        id = container.newChess(p1,p2);
                    }
                    container.getChessGame(id).setTextChannel(mre.getChannel());
                }
                if(split.length==2) {
                    String fen = split[1];
                    if(split[0].toLowerCase().equals("cpu")) {
                        boolean random = Chess.randomizeSide();
                        Player p1 = new Player(random, true, mre.getAuthor().getIdLong());
                        id = container.newChess(p1, fen);
                        if(id==-1) return;
                        container.getChessGame(id).setTextChannel(mre.getChannel());
                        def=false;
                    }
                    if(!isInGuild(idAsLong(split[1]),mre) ) return;
                    boolean random = Chess.randomizeSide();
                    Player p1 = new Player(random, true, mre.getAuthor().getIdLong());
                    Player p2 = new Player(random, true, idAsLong(args));
                    id = container.newChess(p1,p2, fen);
                    if(id==-1) return;
                    container.getChessGame(id).setTextChannel(mre.getChannel());
                    def=false;
                }
                System.out.println("Chess #"+id+", p1="+container.getChessGame(id).getPlayers()[0].getId()+"("+container.getChessGame(id).getPlayers()[0].isWhiteSide()+"), p2="+container.getChessGame(id).getPlayers()[1].getId()+"("+container.getChessGame(id).getPlayers()[1].isWhiteSide()+")");
                try {
                    Chess game = container.getChessGame(id);
                    Board board = game.getBoard();
                    if(!game.getPlayers()[1].isHumanPlayer()&&!game.getCurrentTurn().isHumanPlayer()) {
                        Stockfish client = Kebbot.client;
                        String botMove = client.getBestMove(game.getFEN(), 2000);
                        boolean white = game.getCurrentTurn().isWhiteSide();
                        int[] pos = board.getPos(botMove, white);
                        Piece piece = null;
                        if(pos.length==5) {
                            switch (pos[4]) {
                                case 1 -> piece = new Queen(white);
                                case 2 -> piece = new Rook(white);
                                case 3 -> piece = new Knight(white);
                                case 4 -> piece = new Bishop(white);
                                default -> {}
                            }
                        }
                        if(game.playerMove(game.getCurrentTurn(),pos[0],pos[1],pos[2],pos[3], piece)) {
                            ImageHandler.updateIMGBoard(board);
                            File file = new File(ImageHandler.alboard);
                            EmbedBuilder result = new EmbedBuilder();
                            String title = def ? "Using the Default board":"Using a custom board";
                            result.setTitle("Chess Game");
                            result.setAuthor(title);
                            game.title=(title);
                            String turn = "";
                            if (game.getChecks()[0]) {
                                turn += "White is in Check.\n";
                            }
                            if (game.getChecks()[1]) {
                                turn += "Black is in Check.\n";
                            }
                            String values = Kebbot.client.getEvalScore(game.getFEN(), 1000);
                            turn += "It's ";
                            turn += game.getCurrentTurn().isWhiteSide() ? "White" : "Black";
                            turn += "'s turn. ";
                            turn += game.getCurrentTurn().isWhiteSide() ? "Black" : "White";
                            result.setFooter(turn + "'s move was: " + botMove + ".\nValue of Board: " + values+ ".");
                            String description = mre.getMessage().getAuthor().getName()+" created a board ";
                            description += def ? "with default settings.":"with FEN settings: "+split[1];
                            mre.getChannel().sendMessage(description).queue();
                            mre.getChannel().sendMessage(result.build())
                                    .addFile(file, "alboard.png")
                                    .queue((message) -> {
                                        game.setChessID(message.getIdLong());
                                    });
                            mre.getMessage().delete().queue();
                        }
                        game.setCurrentTurn(!game.getCurrentTurn().isWhiteSide());
                        return;
                    }
                    ImageHandler.updateIMGBoard(board);
                    File file = new File(ImageHandler.alboard);
                    EmbedBuilder result= new EmbedBuilder();
                    String title = def ? "Using the Default board":"Using a custom board";
                    result.setTitle("Chess Game");
                    result.setAuthor(title);
                    game.title=(title);
                    result.setImage("attachment://alboard.png");
                    String turn = "";
                    if(game.getChecks()[0]) {
                        turn+="White is in Check.\n";
                    }
                    if(game.getChecks()[1]) {
                        turn+="Black is in Check.\n";
                    }
                    String values = Kebbot.client.getEvalScore(game.getFEN(), 1000);
                    turn += "It's ";
                    turn += game.getCurrentTurn().isWhiteSide() ? "White":"Black";
                    turn += "'s turn. ";
                    result.setFooter(turn+"\nValue of Board: "+values+".");
                    String description = mre.getMessage().getAuthor().getName()+" created a board ";
                    description += def ? "with default settings.":"with FEN settings: "+split[1];
                    mre.getChannel().sendMessage(description).queue();
                    mre.getChannel().sendMessage(result.build())
                            .addFile(file, "alboard.png")
                            .queue((message) -> {
                                game.setChessID(message.getIdLong());
                            });
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error here boyo");
                    mre.getMessage().delete().queue();
                    mre.getChannel().sendMessage("That board is not valid // There has been an error").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
                }
            }
        }
    }

    /**
     * Executes an action if a reaction has been added to a message.
     * This is currently used for controlling a Yeelight Device,
     * and also creating a "lobby" for the CodeWords Game.
     *
     * @param gmrae the Guild Message Reaction Add Event
     * @see #onReactionRemove(GuildMessageReactionRemoveEvent)
     * @see YeeLight
     */
    public static void onReactionAdd(GuildMessageReactionAddEvent gmrae) {
        String raw = gmrae.getChannel().retrieveMessageById(gmrae.getMessageId()).complete().getContentRaw();
        String[] firstWords = raw.split(" ", 4);
        if(firstWords.length!=4) return;
        raw = firstWords[0] + " " + firstWords[1] + " " + firstWords[2];
        switch (raw) {
            // Complete message would be: "Use the reactions bellow to control the lights!", but it will be edited a lot of times
            case "Use the reactions" -> {
                if (Kebbot.Light == null || gmrae.getMember().equals(gmrae.getGuild().getSelfMember())) return;
                if (gmrae.getChannel().retrieveMessageById(gmrae.getMessageId()).complete().getMember().equals(gmrae.getGuild().getSelfMember())) {
                    switch (gmrae.getReactionEmote().getEmoji()) {
                        case "\uD83D\uDCA1" -> {
                            try {
                                Kebbot.Light.setPower(true);
                                gmrae.getChannel().retrieveMessageById(gmrae.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light)).queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        case "\uD83D\uDD34" -> {
                            try {
                                Kebbot.Light.setRGB(255, 0, 0);
                                gmrae.getChannel().retrieveMessageById(gmrae.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light)).queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        case "\uD83D\uDD35" -> {
                            try {
                                Kebbot.Light.setRGB(0, 0, 255);
                                gmrae.getChannel().retrieveMessageById(gmrae.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light)).queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        case "\uD83D\uDFE2" -> {
                            try {
                                Kebbot.Light.setRGB(0, 255, 0);
                                gmrae.getChannel().retrieveMessageById(gmrae.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light)).queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        case "???" -> {
                            try {
                                Kebbot.Light.toggle();
                                Thread.sleep(600);
                                Kebbot.Light.toggle();
                                Thread.sleep(600);
                                Kebbot.Light.toggle();
                                Thread.sleep(600);
                                Kebbot.Light.toggle();
                                gmrae.getChannel().retrieveMessageById(gmrae.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light) + "  (Owner has been notified)").queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        case "???" -> {
                            try {
                                int br = Integer.parseInt(Kebbot.Light.getProperties().get(YeelightProperty.BRIGHTNESS));
                                Kebbot.Light.setBrightness(br + 10);
                                gmrae.getChannel().retrieveMessageById(gmrae.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light) + "  (Added 10% Brightness)").queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        case "???" -> {
                            try {
                                int br = Integer.parseInt(Kebbot.Light.getProperties().get(YeelightProperty.BRIGHTNESS));
                                Kebbot.Light.setBrightness(br - 10);
                                gmrae.getChannel().retrieveMessageById(gmrae.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light) + "  (Removed 10% Brightness)").queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            // Complete message would be (example): "CodeWords: Please click on the Plus sign below to join!   Game ID: 0"
            case "CodeWords: Please click" -> {
                if (gmrae.getChannel().retrieveMessageById(gmrae.getMessageId()).complete().getMember().equals(gmrae.getGuild().getSelfMember())) {
                    if(gmrae.getUser().isBot()||container.getCWGameID(gmrae.getUserIdLong())!=-1) return;
                    String[] split = raw.split(" ");
                    int id = Integer.parseInt(split[split.length-1]);
                    CodeWords game = container.getCWGame(id);
                    switch (gmrae.getReactionEmote().getEmoji()) {
                        case "???" -> {
                            if(game.getPlayers().size()>=10) {
                                gmrae.getChannel().sendMessage("Game ID: "+id+" - The lobby is full. No more players can join").queue();
                                return;
                            }
                            game.addPlayers(gmrae.getUserIdLong());
                        }
                        case "\uD83D\uDFE2" -> {
                            if(game==null||game.getPlayers().size()<4) {
                                gmrae.getChannel().sendMessage("Game ID: "+id+" - The lobby has not been created / There are not enough players").queue();
                                return;
                            }
                            game.setTeams();
                            String red = "";
                            String blue = "";
                            for(int i=0;i<game.getTeamRed().size();i++) {
                                if(i!=0&&i!=game.getTeamRed().size()-1) {
                                    red+="<@!" + game.getTeamRed().get(i)+">, ";
                                } else if(i==0){
                                    red+="( <@!" + game.getTeamRed().get(i)+"> (Spymaster), ";
                                } else {
                                    red+="<@!" + game.getTeamRed().get(i)+">)";
                                }

                            }
                            for(int i=0;i<game.getTeamBlue().size();i++) {
                                if(i!=0&&i!=game.getTeamBlue().size()-1) {
                                    blue+="<@!" + game.getTeamBlue().get(i)+">, ";
                                } else if(i==0){
                                    blue+="( <@!" + game.getTeamBlue().get(i)+"> (Spymaster), ";
                                } else {
                                    blue+="<@!" + game.getTeamBlue().get(i)+">)";
                                }

                            }
                            gmrae.getChannel().sendMessage("The teams have been created (randomly)\n+" +
                                    "Team Red "+red+"    and    Team Blue "+blue).queue();
                            gmrae.getGuild().getMemberById(game.getTeamBlue().get(0)).getUser().openPrivateChannel().queue((channel) ->
                            {
                                game.setBlueSpyID(channel.getLatestMessageIdLong());
                                channel.sendMessage(game.spyInfo()).queue();
                            });
                            gmrae.getGuild().getMemberById(game.getTeamRed().get(0)).getUser().openPrivateChannel().queue((channel) ->
                            {
                                game.setRedSpyID(channel.getLatestMessageIdLong());
                                channel.sendMessage(game.spyInfo()).queue();
                            });
                            gmrae.getChannel().sendMessage(game.toString()).queue();
                            gmrae.getChannel().sendMessage("Red Spymaster, please select a word and the number of cards that are related (Example: Water 3)").queue();
                        }
                        default -> {
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * Executes an action if a reaction has been removed from a message.
     * This is currently used for controlling a Yeelight Device.
     *
     * @param gmrre the Guild Message Reaction Remove Event
     * @see #onReactionAdd(GuildMessageReactionAddEvent)
     * @see YeeLight
     */
    public static void onReactionRemove(GuildMessageReactionRemoveEvent gmrre) {
        String raw = gmrre.getChannel().retrieveMessageById(gmrre.getMessageId()).complete().getContentRaw();
        String[] firstWords = raw.split(" ", 4);
        if(firstWords.length!=4) return;
        raw = firstWords[0] + " " + firstWords[1] + " " + firstWords[2];
        switch (raw) {
            case "Use the reactions" -> {
                if (Kebbot.Light == null || gmrre.getMember().equals(gmrre.getGuild().getSelfMember())) return;
                if (gmrre.getChannel().retrieveMessageById(gmrre.getMessageId()).complete().getMember().equals(gmrre.getGuild().getSelfMember())) {
                    switch (gmrre.getReactionEmote().getEmoji()) {
                        case "\uD83D\uDCA1" -> {
                            try {
                                Kebbot.Light.setPower(false);
                                gmrre.getChannel().retrieveMessageById(gmrre.getMessageId()).complete().editMessage(YeeLight.getBaseProp(Kebbot.Light)).queue();
                            } catch (Exception ignored) { }
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
    public static Long idAsLong(String id) {
        if (id.length() == 22) {
            return Long.parseLong(id.substring(3, 21));
        } else {
            return (null);
        }
    }

    /**
     * Given a user's ID as a Long, checks if that user
     * is a member of the guild where a Guild Message
     * Received Event occurred.
     *
     * @param id The user's ID (as a Long)
     * @param gmre the GuildMessageReceivedEvent
     * @return if the user is a Guild Member
     */
    private static boolean isInGuild(Long id, GuildMessageReceivedEvent gmre) {
        if(id==null) return false;
        List<Member> members=gmre.getGuild().getMembers();
        for(Member member: members) {
            Long memberID=member.getIdLong();
            if(memberID.equals(id)) return true;
        }
        return false;
    }
    
    /**
     * Gets the full message history of a Discord Server
     * (given the API limitations this will take a while).
     * Given a message it will get it's channel.
     *
     * @param mre the MessageReceivedEvent
     * @return the full array of messages
     */
    private static ArrayList<Message> fullHistory(MessageReceivedEvent mre) {
        ArrayList<Message> history = new ArrayList<>();
        String oldMessageID = mre.getChannel().getLatestMessageId();
        boolean done = false;
        while(!done) {
            ArrayList<Message> messages = new ArrayList<>(mre.getChannel().getHistoryBefore(oldMessageID,100).complete().getRetrievedHistory());
            if(messages.size()<100) done=true;
            history.addAll(messages);
            oldMessageID=messages.get(messages.size()-1).getId();
        }
        return history;
    }

    /**
     * Given a search term and a message event
     * it will search for it on the channel history.
     *
     * @param search the search term
     * @param mre the MessageReceivedEvent
     * @return
     */
    private static ArrayList<String> search(String search, MessageReceivedEvent mre) {
        ArrayList<Message> history = fullHistory(mre);
        mre.getChannel().sendMessage("Searched in a total of "+history.size()+" messages!").queue();
        System.out.println(history.size());
        ArrayList<String> result = new ArrayList<>();
        for(Message message:history) {
            if(message.getContentRaw().toLowerCase().contains(search.toLowerCase())) result.add(message.toString());
        }
        return result;
    }
}
