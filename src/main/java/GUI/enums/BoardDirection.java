package GUI.enums;

public enum BoardDirection
{
    PLAYERNORTH(0), PLAYERSOUTH(7);

    private int value;

    BoardDirection(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}


