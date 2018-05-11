package pl.epiklp.whiteboard;


import android.graphics.Path;

/**
 * Created by epiklp on 11.05.18.
 */

public class FingerPath {
    public int color;
    public boolean blur;
    public boolean emboss;
    public int strokeWidth;
    public Path mPath;

    public FingerPath(int color, boolean blur, boolean emboss, int strokeWidth, Path mPath) {
        this.color = color;
        this.blur = blur;
        this.emboss = emboss;
        this.strokeWidth = strokeWidth;
        this.mPath = mPath;
    }
}
