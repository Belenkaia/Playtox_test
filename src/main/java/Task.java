public class Task implements Runnable{
    private String idFrom;
    private String idTo;
    private Integer sendMoney;

    public Task(String idFrom, String idTo, Integer sendMoney)
    {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.sendMoney = sendMoney;
    }
    public void run() {
        Main.accountMap.get(idFrom).decrementMoney(sendMoney);
        Main.accountMap.get(idTo).incrementMoney(sendMoney);
    }



}
