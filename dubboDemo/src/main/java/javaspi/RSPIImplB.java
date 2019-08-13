package javaspi;

public class RSPIImplB implements RSPI {
    @Override
    public void say(String id) {
        System.out.println("B " + id);
    }
}
