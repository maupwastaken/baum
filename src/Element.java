public class Element
{
    public int index;
    public Element smaller;
    public Element bigger;
    public int smallerDepth;
    public int biggerDepth;
    
    public Element(int index)
    {
        this.index=index;
    }
}
