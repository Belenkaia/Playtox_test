public class Task implements Runnable{
    private String idFrom;
    private String idTo;
    private int sleepTime;

    public void run() {

    }

    public void setIdFrom(String idFrom) {
        this.idFrom = idFrom;
    }

    public String getIdFrom() {
        return idFrom;
    }

    public void setIdTo(String idTo) {
        this.idTo = idTo;
    }

    public String getIdTo() {
        return idTo;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getSleepTime() {
        return sleepTime;
    }
}
