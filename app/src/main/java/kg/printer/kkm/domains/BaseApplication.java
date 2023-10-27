package kg.printer.kkm.domains;

import android.app.Application;

import kg.printer.kkm.repositories.DatabaseDAO;

import com.rt.printerlibrary.printer.RTPrinter;

public class BaseApplication extends Application {

    public static BaseApplication instance = null;
    private RTPrinter rtPrinter;

    @DatabaseDAO.BaseEnums.CmdType
    private int currentCmdType = DatabaseDAO.BaseEnums.CMD_ESC;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static BaseApplication getInstance(){
        return instance;
    }

    public RTPrinter getRtPrinter() {
        return rtPrinter;
    }

    public void setRtPrinter(RTPrinter rtPrinter) {
        this.rtPrinter = rtPrinter;
    }

    @DatabaseDAO.BaseEnums.CmdType
    public int getCurrentCmdType() {
        return currentCmdType;
    }

    public void setCurrentCmdType(@DatabaseDAO.BaseEnums.CmdType int currentCmdType) {
        this.currentCmdType = currentCmdType;
    }

}
