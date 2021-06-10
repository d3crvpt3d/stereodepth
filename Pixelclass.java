public class Pixelclass
{



    //Var
    private float value_left;
    private float value_right;
    private int stelle;
    private float distance;

    private int value_alt;
    private int value_alt2;

    private float[] hsb_l = new float[3];
    private float[] hsb_r = new float[3];


    //getMethods
    public int getStelle()
    {

        return stelle;
    }

    public float getValue_left() {
        return value_left;
    }

    public float getValue_right() {
        return value_right;
    }

    public float getDistance()
    
    {
        return distance;
    }

    public int getValue_alt()
    {

        return value_alt;
    }

    public int getValue_alt2()
    {

        return value_alt2;
    }

    public float getH_l() {
        return hsb_l[0];
    }

    public float getH_r() {
        return hsb_r[0];
    }



    //setMethods
    public void setValue_left(float value_left) {
        this.value_left = value_left;
    }

    public void setValue_right(float value_right) {
        this.value_right = value_right;
    }

    public void setStelle(int stelle)
    {

        this.stelle = stelle;
    }

    public void setDistance(Float distance)
    {

        this.distance = distance;
    }

    public void setValue_alt(int value_alt)
    {

        this.value_alt = value_alt;
    }

    public void setValue_alt2(int value_alt2)
    {
        
        this.value_alt2 = value_alt2;
    }

    public void setHsb_l(float[] hsb_l) {
        this.hsb_l = hsb_l;
    }

    public void setHsb_r(float[] hsb_r) {
        this.hsb_r = hsb_r;
    }











}
