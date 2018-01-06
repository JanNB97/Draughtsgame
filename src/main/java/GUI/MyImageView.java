package GUI;

import javafx.scene.image.ImageView;

public class MyImageView extends ImageView
{
    private int fieldXPos, fieldYPos;

    public MyImageView(int fieldXPos, int fieldYPos)
    {
        super();
        this.fieldXPos = fieldXPos;
        this.fieldYPos = fieldYPos;
    }

    public int getFieldXPos()
    {
        return fieldXPos;
    }

    public int getFieldYPos()
    {
        return fieldYPos;
    }
}
