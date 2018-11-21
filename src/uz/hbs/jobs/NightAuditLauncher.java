package uz.hbs.jobs;

public class NightAuditLauncher {
	
	public NightAuditLauncher() {
	}
	
	public void start(){
		new Thread(new NightAudit()).start();
	}
	
	public static void main(String[] args) {
		NightAuditLauncher launcher = new NightAuditLauncher();
		launcher.start();
	}

}
