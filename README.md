# TelePad

Simple custom warp & teleportation plugin for Bukkit on Minecraft (MCv1.12).

TelePad features the ability to travel between locations and worlds via simple command. The plugin has no dependencies and will likely have no effect on any other plugins already installed, even other teleport plugins! Always back-up to be sure, though.

DOWNLOAD (plugin JAR file): https://github.com/randallarms/telepad/blob/master/resources/jar/TelePad.jar

Config file for custom configurations can be found in the "options.yml" file generated after the first time loading TelePad on a server. Changing the values in this file may change how your server works, so please be sure to back up any server data before editing the config. To reset the config file, simply delete it while the server is off, and then restart your server. A new file should be generated.

Now supporting console commands!

commands: 

     telepad:
        description: Info on the TelePad plugin.
        usage: /<command>
     tele:
        description: Teleport to a set location.
        usage: /<command> <name>
        aliases: [tp]
     jump:
        description: Jump a short (~< 100m) distance.
        usage: /<command>
     teledel:
        description: Delete a teleport.
        usage: /<command> <name>
        aliases: [teledelete, tpdelete, tpdel]
     telelist:
        description: List teleports.
        usage: /<command>
        aliases: [tplist]
     teleset:
        description: Set a teleport.
        usage: /<command> <name>
        aliases: [tpset]
     opReqTP:
        description: Set the option to allow TP commands for OPs only.
        usage: /<command> <on/off>
        aliases: [opreqtp, oprequiredtp]
     permsReqTP:
        description: Set the option to allow TP commands for OPs only.
        usage: /<command> <on/off>
        aliases: [permsreqtp, permsrequiredtp]
     sparkles:
        description: Set the option to allow sparkle effects for teleports.
        usage: /<command> <on/off>
		
permissions:

     telepad.*:
        description: Permission for all TelePad commands.
        children:
           telepad.tp: true
           telepad.telelist: true
           telepad.teleset: true
           telepad.teledel: true
           telepad.jump: true
           telepad.sparkles: true
     telepad.tp:
        description: Permission to use the 'tele' command.
     telepad.telelist:
        description: Permission to use the 'telelist' command.
     telepad.teleset:
        description: Permission to use the 'teleset' command.
     telepad.teledel:
        description: Permission to use the 'teledel' command.
     telepad.jump:
        description: Permission to use the 'jump' command.
     telepad.sparkles:
        description: Permission to have a sparkling effect on teleport.

Any forks, branches, and pull requests are welcome! Please feel free to voice criticism to better the project, as well.

Got a problem? Bug, glitch, complaint? Visit the Issues page and let me know, please.