/**
 * This class serves as a blueprint for what a
 * command for the bot must have as properties.
 * It is used because most commands for the discord
 * bot abide by similar rules and also the code looks
 * cleaner and is easier to understand
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Bot
 * @see CommandList
 */

public class Command {
    Long[] allowedServersID = {};           //stores the list of servers where the command is allowed
    Long[] allowedTextChannelsID= {};       //List of allowed text channels
    Long[] roleIDs= {};                     //stores the allowed roles
    Long[] userIDs= {};                     //stores the allowed users
    String[] aliases;                       //list of the possible ways the command starts, ex: !command, -command, .command would be in this case {"!","-","."}
    String name;                            //the command name aka the way it will be addressed. ex: if the name is ping, to access it you would need type !ping
    String description;                     //basic description and/or help for the command
    boolean enabled;                        //whether or not the command is enabled (the default is true)
    boolean canDisable;                     //whether or not the command can be disabled (if not, the above boolean is always set to 1)
    //defaultAliases are multiple ways I want the bot to be addressed by default (with a space afterwards for ease in usage), and adminID is the bot owner's ID
    public static final String[] DEFAULT_ALIASES = {"!kebbot ", ".kebbot ", "-kebbot ", "!kb ", ".kb ", "-kb "};
    //this id must be changed to the admin's ID
    public static final Long ADMIN_ID =123456789123456789L;

    /**
     * Default constructor for a command, all of its properties
     * must be specified
     *
     * @param allowedServersID list of the allowed servers' IDs
     * @param allowedTextChannelsID list of the allowed text channels' IDs
     * @param roleIDs list of the allowed roles' IDs
     * @param userIDs list of the allowed users' IDs
     * @param aliases list of allowed aliases for commands (the prefix for a command)
     * @param name the name for the command
     * @param description a brief description of what the command does and how to use it
     * @param enabled whether or not the command is enabled (can be used)
     * @param canDisable whether or not the command can be disabled (forces enabled to always be true)
     * @see #Command(Long[], Long[], Long[], Long[], String, String, boolean)
     * @see #Command(String, boolean, String)
     */
    public Command(Long[] allowedServersID, Long[] allowedTextChannelsID, Long[] roleIDs, Long[] userIDs, String[] aliases, String name, String description, boolean enabled, boolean canDisable){
        this.allowedServersID = allowedServersID;
        this.allowedTextChannelsID = allowedTextChannelsID;
        this.roleIDs = roleIDs;
        this.userIDs = userIDs;
        this.aliases = aliases;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
        this.canDisable = canDisable;
    }

    /**
     * Similar to the default constructor but with some default values
     * (uses the default aliases and enables the command)
     *
     * @param allowedServersID list of the allowed servers' IDs
     * @param allowedTextChannelsID list of the allowed text channels' IDs
     * @param roleIDs list of the allowed roles' IDs
     * @param userIDs list of the allowed users' IDs
     * @param name the name for the command
     * @param description a brief description of what the command does and how to use it
     * @param canDisable whether or not the command can be disabled (forces enabled to always be true)
     * @see #Command(Long[], Long[], Long[], Long[], String[], String, String, boolean, boolean)
     * @see #Command(String, boolean, String)
     */
    public Command(Long[] allowedServersID, Long[] allowedTextChannelsID, Long[] roleIDs, Long[] userIDs, String name, String description, boolean canDisable){
        this.allowedServersID = allowedServersID;
        this.allowedTextChannelsID = allowedTextChannelsID;
        this.roleIDs = roleIDs;
        this.userIDs = userIDs;
        this.aliases = DEFAULT_ALIASES;
        this.description = description;
        this.name = name;
        this.enabled = true;
        this.canDisable = canDisable;
    }

    /**
     * Constructor with most of the values set by default
     *
     * @param name the name for the command
     * @param forAdmin whether or not the specified command can be used only by the Admin
     * @param description a brief description of what the command does and how to use it
     * @see #Command(Long[], Long[], Long[], Long[], String, String, boolean)
     * @see #Command(Long[], Long[], Long[], Long[], String[], String, String, boolean, boolean)
     */
    public Command(String name, boolean forAdmin, String description){
        if(!forAdmin) {
            //for normal users, we use the values bellow
            //this.allowedServersID, this.allowedTextChannels, this.roleIDs, this.userIDs won't be needed as we will set everything as allowed by default (anyone can type the command anywhere, as long as the bot can read the event)
            this.aliases = DEFAULT_ALIASES;
            this.name = name;
            this.description = description;
            this.enabled = true;
            this.canDisable = true;
        } else {
            //for admin commands, we use the values bellow
            //this.allowedServersID isn't needed, as the admin can just choose where to type these commands
            //this.allowedTextChannels also isn't needed, same reason as above
            //this.roleIDs also isn't needed, same reason as above
            this.userIDs = new Long[]{ADMIN_ID};
            this.aliases = DEFAULT_ALIASES;
            this.name = name;
            this.description = description;
            this.enabled = true;
            this.canDisable = false;
        }
    }
}
