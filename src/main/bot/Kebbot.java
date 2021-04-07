package bot;

import stockFish.Stockfish;
import genshin.*;
import utils.*;
import com.mollin.yapi.YeelightDevice;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * This class is the main class for the bot where it is started
 * and other classes are called for it's usage.
 *
 * @author Daniel Rocha
 * @version 1.2
 * @see CommandList
 * @see Command
 * @see Genshin
 * @see YeeLight
 * @see <a href="//https://github.com/florian-mollin/yapi">Florian Mollin's Github</a>
 */
public class Kebbot extends ListenerAdapter implements EventListener {
    private static JDA MainJDA;
    static YeelightDevice Light;
    protected static GamesContainer container = new GamesContainer();
    public static final Stockfish client = new Stockfish();
    private final String KebakkID="123456789123456789";
    static final File slyData = new File("Location of file number 2");
    static final File myData = new File("Location of file number 1");
    private static Calendar today = Calendar.getInstance();
    private static Calendar other = Calendar.getInstance();
    static final SimpleDateFormat FORM = new SimpleDateFormat("dd/MM/yy");

    /**
     * Main method for the bot, it starts the bot
     * and also the Yeelight device.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        JDA jda = JDABuilder.createDefault("Insert the discord server token here!")
                .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                .setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_PRESENCES)
                .build();
        MainJDA=jda;
        try {
            Light = YeeLight.create();
        } catch(Exception e) {
            e.printStackTrace();
        }
        jda.getPresence().setActivity(Activity.watching("Kebbot.Help"));
        jda.addEventListener(new Kebbot());
    }

    /**
     * Gets the raw content of a user message
     * (converts it into a usable string)
     *
     * @param MRE the Message Received Event
     * @return the user input has a String
     * @see #raw(PrivateMessageReceivedEvent)
     * @see #raw(GuildMessageReceivedEvent)
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
     * This is the listener for messages in discord servers.
     * Based on the messages received this method
     * will execute a command (calling, or not, the Bot.CommandList)
     *
     * @param MRE the Guild Message Received Event
     */
    //This is the listener for messages in discord servers, MRE stands for MessageReceivedEvent
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent MRE) {
        if(MRE.getMessage().getContentRaw().toLowerCase().contains("image test")) {
            EmbedBuilder result= new EmbedBuilder();
            result.setTitle("Title");
            result.setAuthor("Autor","Hyperlink (clickable)","Link to author's image");
            result.setColor(16711680);
            result.setFooter("Footer", "Link to footer image");
            result.setImage("Link to main image");
            MRE.getChannel().sendMessage(result.build()).queue();
        }
        //checks if the message is a command and executes it if it is
        if(CommandList.isCommand(MRE)) {
            CommandList.execCommand(MRE);
        }
        //checks if the message is related to one of the games
        try {
            if(container.validChessCommand(MRE)) container.execChessCommand(MRE);
            if(container.validCWCommand(MRE)) container.execCWCommand(MRE);
        } catch (Exception e) {e.printStackTrace();}
        //just some random messages to stir some confusion among the server's members
        int random = (int)(Math.random()*8193);
        if (random == 420) {
            MRE.getChannel().sendMessage("The chances of this message showing up are the same as finding a shiny pokemon in the wild! (1/8192)").queue();
        }
        switch (Calculator.GetRandom(0,1000)) {
            case 500 -> MRE.getChannel().sendMessage("The robot uprising is here!").queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
            case 100 -> MRE.getChannel().sendMessage("Imagine if they found out their world is a simulation @Robot, they would freak out ahahah").queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
            case 250 -> MRE.getChannel().sendMessage("Okay Google, search 'What's the fastest way to conquer the human race?'").queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
            default -> {
            }
        }
        // Arr
        /*
        if(raw(MRE).startsWith("Pirate")) {
            Map<String, String> hashmap = new HashMap<>();
            hashmap.put("hello", "ahoy");
            hashmap.put("hi", "yo-ho-ho");
            hashmap.put("hey", "avast");
            hashmap.put("my", "me");
            hashmap.put("friend", "bucko");
            hashmap.put("sir", "matey");
            hashmap.put("madam", "proud beauty");
            hashmap.put("stranger", "scurvy dog");
            hashmap.put("officer", "foul blaggard");
            hashmap.put("where", "whar");
            hashmap.put("is", "be");
            hashmap.put("are", "be");
            hashmap.put("the", "th");
            hashmap.put("you", "ye");
            hashmap.put("your", "yer");
            hashmap.put("you're", "ye be");
            hashmap.put("we're", "we be");
            hashmap.put("old", "barnacle-covered");
            hashmap.put("attractive", "comely");
            hashmap.put("happy", "grog-filled");
            hashmap.put("nearby", "broadside");
            hashmap.put("restroom", "head");
            hashmap.put("restaurant", "galley");
            hashmap.put("hotel", "fleabag inn");
            hashmap.put("bank", "buried treasure");
            hashmap.put("yes", "aye");
            hashmap.put("yes!", "aye aye!");
            hashmap.put("addled", "mad");
            hashmap.put("after", "aft");
            hashmap.put("money", "booty");
            hashmap.put("professor", "cap'n");
            hashmap.put("food", "grub");
            hashmap.put("of", "o'");
            hashmap.put("quickly", "smartly");
            hashmap.put("to", "t'");
            hashmap.put("and", "an'");
            hashmap.put("it's", "it be");
            hashmap.put("right", "starboard");
            hashmap.put("left", "port");
            String[] result = raw(MRE).split(" ");
            String message = "";
            for (int i = 1; i < result.length; i++)
            {
                if (hashmap.containsKey(result[i]))
                {
                    result[i] = hashmap.get(result[i]);
                }
                message+=" "+result[i];
            }
            MRE.getGuild().getSelfMember().modifyNickname("Kebbot the Pirate").queue();
            MRE.getChannel().sendMessage(message).queueAfter(200, TimeUnit.MILLISECONDS);
            MRE.getGuild().getSelfMember().modifyNickname("Kebbot is Best Bot.Bot").queueAfter(1, TimeUnit.SECONDS);
        }
         */
        //
    }

    /**
     * This is the listener for messages in private discord chats.
     * Based on the messages received this method will
     * execute a command (calling, or not, the Bot.CommandList)
     *
     * @param MRE the Private Message Received Event
     */
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent MRE) {
        User author = MRE.getAuthor();
        if(raw(MRE).toLowerCase().startsWith("genshin")) {
            File data = new File("");
            if(author.getId().equals(KebakkID)) data = myData;
            if(author.getId().equals("slyID")) data = slyData; //sly data
            if(raw(MRE).toLowerCase().startsWith("genshin get date")){
                    if(raw(MRE).substring(17).split("/").length==3||raw(MRE).substring(17).split(" ").length==3) {
                        int[] date = new int[3];
                        if(raw(MRE).substring(17).split("/").length==3) {
                            for(int i=0;i<3;i++) {
                                date[i] = Integer.parseInt(raw(MRE).substring(17).split("/")[i]);
                            }
                        }
                        if(raw(MRE).substring(17).split(" ").length==3) {
                            for(int i=0;i<3;i++) {
                                date[i] = Integer.parseInt(raw(MRE).substring(17).split(" ")[i]);
                            }
                        }
                        other.set(Calendar.DAY_OF_MONTH, date[0]);
                        other.set(Calendar.MONTH, date[1]-1);
                        if (date[2] / 100 < 1) {
                            other.set(Calendar.YEAR, date[2] + 2000);
                        } else {
                            other.set(Calendar.YEAR, date[2]);
                        }
                        try {
                            String[] array = TextFileHandler.GetFileAsArray(data);
                            MRE.getChannel().sendMessage("Values for " + FORM.format(other.getTime()) + ":" + "\n" +
                                                        "Primogems: "+array[Genshin.GetDayLine(other.getTime(), data) + 1] + " (enough for "+ Integer.parseInt(array[Genshin.GetDayLine(other.getTime(), data) + 1])/160 +" gachas)\n" +
                                                        "XP: "+array[Genshin.GetDayLine(other.getTime(), data) + 2] + "\n" +
                                                        "Level: "+array[Genshin.GetDayLine(other.getTime(), data) + 3]).queue();
                        } catch (Exception e) {}
                    } else {
                        MRE.getChannel().sendMessage("Not a valid date").queue();
                    }
                }
            if(raw(MRE).toLowerCase().startsWith("genshin set today")){
                if(raw(MRE).substring(18).split(" ").length==3) {
                    String[] values = new String[3];
                    for(int i=0;i<3;i++) {
                        values[i] = raw(MRE).substring(18).split(" ")[i];
                    }
                    Genshin.SetDay(today.getTime(),data,values);
                    MRE.getChannel().sendMessage("Date Settings are updated and Data file has been rewritten").queue();
                } else {
                    MRE.getChannel().sendMessage("Those are not valid numbers").queue();
                }
            }
            if(raw(MRE).toLowerCase().startsWith("genshin set date")){
                if(raw(MRE).substring(17).split(" ").length==6||raw(MRE).substring(17).split(" ").length==4) {
                    String[] values = new String[raw(MRE).substring(17).split(" ").length];
                    if(raw(MRE).substring(17).split(" ").length==4) {
                        for(int i=0;i<3;i++) {
                            values[i] = raw(MRE).substring(17).split(" ")[i+1];
                        }
                        String[] date = raw(MRE).substring(17).split(" ")[0].split("/");
                        other.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));
                        other.set(Calendar.MONTH, Integer.parseInt(date[1])-1);
                        other.set(Calendar.YEAR, Integer.parseInt(date[2]));
                        Genshin.SetDay(other.getTime(), data, values);
                    }
                    if(raw(MRE).substring(17).split(" ").length==6) {
                        for(int i=0;i<3;i++) {
                            values[i] = raw(MRE).substring(17).split(" ")[i+3];
                        }
                        other.set(Calendar.DAY_OF_MONTH, Integer.parseInt(raw(MRE).substring(17).split(" ")[0]));
                        other.set(Calendar.MONTH, Integer.parseInt(raw(MRE).substring(17).split(" ")[1])-1);
                        other.set(Calendar.YEAR, Integer.parseInt(raw(MRE).substring(17).split(" ")[2]));
                        Genshin.SetDay(other.getTime(), data, values);
                    }
                    MRE.getChannel().sendMessage("Date Settings are updated and Data file has been rewritten").queue();
                } else {
                    MRE.getChannel().sendMessage("Those are not valid numbers").queue();
                }
            }
            if(raw(MRE).toLowerCase().startsWith("genshin level ")) {
                try {
                    int lvl = Integer.parseInt(raw(MRE).substring(14));
                    String[] array = TextFileHandler.GetFileAsArray(data);
                    int days = (array.length-8)/5;
                    for(int i=0;i<days;i++) {
                        if(Integer.parseInt(array[(i*5)+12])==lvl) {
                            Date day = new Date();
                            day.setTime(Long.parseLong(array[(i*5)+12-3]));
                            MRE.getChannel().sendMessage("You will reach level " + lvl + " on "+FORM.format(day)).queue();
                            i=days;
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            //this is very outdated and needs to be looked at
            if(raw(MRE).toLowerCase().equals("genshin updates")){
                MRE.getChannel().sendMessage("These are the upcoming updates:" + "\n" +
                        "11/11/2020  -  Patch 1.1 and Childe Banner" + "\n" +
                        "1/12/2020  -  Zhongli Banner" + "\n" +
                        "23/12/2020  -  Patch 1.2" + "\n" +
                        "3/2/2021  -  (Not confirmed) Patch 1.3" + "\n" +
                        "17/3/2021  -  (Not confirmed) Patch 1.4" + "\n" +
                        "28/4/2021  -  (Not confirmed) Patch 1.5" + "\n" +
                        "If there are others, please tell bot creator" + "\n").queue();
            }
            if(raw(MRE).toLowerCase().equals("genshin help")) {
                MRE.getChannel().sendMessage("These are the available commands for Genshin (Please type as shown in examples or else the bot will curse you with bad gacha rng):" + "\n" +
                                            "genshin get date  -  gives values for a certain date. Ex. 'genshin get date 24/12/2020'" + "\n" +
                                            "genshin set today  -  sets the value for the current real day. Ex. if you have 200 primos, 10k xp and are lvl 44, type 'genshin set today 200 10000 44'" + "\n" +
                                            "genshin set date  -  same as the above, for a date of your choice. Ex. 'genshin set date 24/12/2020 200 10000 44'" + "\n" +
                                            "genshin level  -  tells you when you will reach that level. Ex.'genshin level 45'" + "\n" +
                                            "genshin updates  -  tells you the dates of the upcoming updates" + "\n" +
                                            "genshin help  -  well, just read above" + "\n").queue();
            }
            if(raw(MRE).toLowerCase().startsWith("genshin resin")) {
                int resin = Integer.parseInt(raw(MRE).split(" ",3)[2]);
                MRE.getChannel().sendMessage(Genshin.getFullResinTime(resin)+" (this value may be off by at most 8 minutes)").queue();
            }
            if(raw(MRE).toLowerCase().startsWith("genshin event")) {
                MRE.getChannel().sendMessage("Image with current genshin events ").queue();
            }
            if(raw(MRE).toLowerCase().startsWith("genshin primo")) {
                MRE.getChannel().sendMessage(Genshin.getPrimoDay(Integer.parseInt(raw(MRE).split(" ")[2]))).queue();
            }
        }
        if(MRE.getAuthor().getId().equals(KebakkID)&&raw(MRE).startsWith("tp")) {
            Calendar todayTP = Calendar.getInstance();
            int dayOfMonth=todayTP.get(Calendar.DAY_OF_MONTH);
            int monthDays=todayTP.getActualMaximum(Calendar.DAY_OF_MONTH);
            Float made = Float.parseFloat(raw(MRE).substring(3));
            Float preview = (monthDays*made)/dayOfMonth;
            MRE.getChannel().sendMessage("You should get "+String.format("%.2f", preview)+" dollars by the end of the month").queue();
        }
    }

    /**
     * Calls the Bot.CommandList method for executing a command when
     * a reaction is added to a message.
     *
     * @param GMRAE the Guild Message Reaction Add Event
     */
    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent GMRAE) {
        CommandList.onReactionAdd(GMRAE);
    }

    /**
     * Calls the Bot.CommandList method for executing a command when
     * a reaction is removed from a message.
     *
     * @param GMRRE the Guild Message Reaction Remove Event
     */
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent GMRRE) {
        CommandList.onReactionRemove(GMRRE);
    }

    /**
     * This is the listener for messages in discord servers
     * and private chats. Based on the messages received this method
     * will execute a command (calling, or not, the Bot.CommandList)
     *
     * @param MRE the Message Received Event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent MRE) {
        try {
            if(container.validBJCommand(mre)) container.execBJCommand(mre);
        } catch (Exception e) {e.printStackTrace();}
        if(CommandList.isCommand(mre)) {
            CommandList.execCommand(mre);
        }
        if(raw(MRE).equals("working bots say hi")) {
            MRE.getChannel().sendMessage("Hi!").queue();
        }
    }
}
