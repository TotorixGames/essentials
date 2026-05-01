package de.kalypzo.essentials.command.chat;

import de.kalypzo.essentials.util.Text;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.processing.CommandContainer;
import org.incendo.cloud.paper.util.sender.Source;

@CommandContainer
public class QuickLinksCommand {


    @Command("discord")
    public void discord(Source source) {
        source.source().sendMessage(Text.deserialize("<newline><prefix> <hl>https://totorix.shop<newline>"));
    }

    @Command("store|shop")
    public void shop(Source source) {
        source.source().sendMessage(Text.deserialize("<newline><prefix> <hl>https://discord.gg/totorix<newline>"));
    }

}
