package GUI;

import javafx.scene.layout.StackPane;

public class MyStackPane extends StackPane
{
    private int x;
    private int y;

    public MyStackPane(int x, int y)
    {
        super();
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
