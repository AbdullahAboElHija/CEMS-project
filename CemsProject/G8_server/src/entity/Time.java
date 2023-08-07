package entity;

public class Time {
	private int hour;
	private int minute;
	private int second;
	private int secondsPassed;
	
//	public int getSecondsPassed() {
//		return secondsPassed;
//	}

//	public void setSecondsPassed(int secondsPassed) {
//		this.secondsPassed = secondsPassed;
//	}

	public Time(int hour, int minute, int second) {
		this.hour=hour;
		this.minute=minute;
		this.second=second;
		
	}
	
	public Time(String currentTime) {
		String[] time= currentTime.split(":");
		hour = Integer.parseInt(time[0]);
		minute = Integer.parseInt(time[1]);
		second= Integer.parseInt(time[2]);
	}
	
	public Time(Integer durationInMin) {
		hour =durationInMin/60;
		minute= durationInMin %60;
		second = 0;
	}

	public String getCurrentTime() {
		return String.format("%02d", hour)+ ":"+ String.format("%02d", minute)+ ":"+String.format("%02d", second);
		//return hour + ":"+ minute+ ":"+second;
	}
	
	public void oneSecondPassedDownCount() {
		if(second !=0 || minute !=0 || hour!= 0) {
			secondsPassed++;
			if(second==0) {
				second=60;
				if(minute==0) {
					minute=60;
					hour--;
				}
				minute--;
			}
			second--;
		}
		
	}
	public void oneSecondPassed() {
		second++;
		if(second==60) {
			minute++;
			second=0;
			if(minute==60) {
				hour++;
				if(hour==24) {
					hour =0;		
				}
			}
		}
	}
	
	public Integer getTimePassedInMinutes() {
		return secondsPassed/60;
	}
	
	public void addTime(Integer addedTime) {

		int total_mins= addedTime+hour*60 +minute;
		hour =total_mins/60;
		minute= total_mins %60;
		
	}
	
	
}
