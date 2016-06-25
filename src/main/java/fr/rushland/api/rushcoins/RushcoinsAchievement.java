package fr.rushland.api.rushcoins;

public class RushcoinsAchievement {

    private String id;
    private String displayName;
    private int quantity;
    private int reward;
    
    public RushcoinsAchievement(String id, String displayName, int quantity, int reward) {
        this.id = id;
        this.displayName = displayName;
        this.quantity = quantity;
        this.reward = reward;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public int getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void addQuantity(int amount) {
        this.quantity += amount;
    }
    
    public int getReward() {
        return this.reward;
    }
    
    public void setReward(int reward) {
        this.reward = reward;
    }
}
