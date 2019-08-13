package javaspi;

public class RSPIImplA implements RSPI {
    @Override
    public void say(String id) {
        System.out.println("A " + id);
    }
}
