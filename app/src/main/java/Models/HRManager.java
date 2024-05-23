package Models;

public class HRManager {

    String hrID, hrKey;

    public HRManager(String hrID, String hrKey) {
        this.hrID = hrID;
        this.hrKey = hrKey;
    }

    public String getHrID() {
        return hrID;
    }

    public void setHrID(String hrID) {
        this.hrID = hrID;
    }

    public String getHrKey() {
        return hrKey;
    }

    public void setHrKey(String hrKey) {
        this.hrKey = hrKey;
    }
}
