package hu.zfall.cleancode.munkaido.domain;

public class WorkTimeItem {
    private String           username;

    private String           day;

    private String           startitem;

    private String           enditem;

    private WorkTimeItemSpecial           special;

    public WorkTimeItem() {
    	
    }

	public WorkTimeItem(String username, String day, String startitem, String enditem, WorkTimeItemSpecial special) {
		this.username = username;
		this.day = day;
		this.startitem = startitem;
		this.enditem = enditem;
		this.special = special;
	}
	
	public WorkTimeItem(String username, String day, String startitem, String enditem, String special) {
		this.username = username;
		this.day = day;
		this.startitem = startitem;
		this.enditem = enditem;
		this.special = special == null ? null : WorkTimeItemSpecial.getByValue(special);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getStartitem() {
		return startitem;
	}

	public void setStartitem(String startitem) {
		this.startitem = startitem;
	}

	public String getEnditem() {
		return enditem;
	}

	public void setEnditem(String enditem) {
		this.enditem = enditem;
	}

	public WorkTimeItemSpecial getSpecial() {
		return special;
	}

	public void setSpecial(WorkTimeItemSpecial special) {
		this.special = special;
	}
    
}
