package kg.printer.kkm.domains;

import android.app.Application;

import kg.printer.kkm.repositories.BaseEnumsDAO;
import com.rt.printerlibrary.printer.RTPrinter;

public class BaseApplication extends Application {

    public static BaseApplication instance = null;
    private RTPrinter rtPrinter;

    @BaseEnumsDAO.CmdType
    private int currentCmdType = BaseEnumsDAO.CMD_ESC;

    @BaseEnumsDAO.ConnectType
    private int currentConnectType = BaseEnumsDAO.NONE;

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

    @BaseEnumsDAO.CmdType
    public int getCurrentCmdType() {
        return currentCmdType;
    }

    public void setCurrentCmdType(@BaseEnumsDAO.CmdType int currentCmdType) {
        this.currentCmdType = currentCmdType;
    }

    @BaseEnumsDAO.ConnectType
    public int getCurrentConnectType() {
        return currentConnectType;
    }

    public void setCurrentConnectType(@BaseEnumsDAO.ConnectType int currentConnectType) {
        this.currentConnectType = currentConnectType;
    }

}