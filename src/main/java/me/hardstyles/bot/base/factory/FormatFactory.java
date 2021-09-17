package me.hardstyles.bot.base.factory;

import com.google.common.base.Strings;
import me.hardstyles.bot.Bot;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class FormatFactory {
    private final Bot bot;
    public FormatFactory(Bot bot){
        this.bot = bot;
    }

    public String formatDate(double milis) {
        String dd = DurationFormatUtils.formatDuration((long) milis, "H:mm:ss", true);
        if(dd.startsWith("0:")) {
            return dd.substring(2);
        }
        return dd;
    }
    public String getProgressBar(long current, long max, int totalBars, String symbol, String otherSymbol, String midPiece) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);
        return Strings.repeat("" + symbol, progressBars) + midPiece + Strings.repeat("" + otherSymbol, totalBars - progressBars);
    }
}
