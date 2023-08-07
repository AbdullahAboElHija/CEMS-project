package entity;

import java.util.Objects;

public class Client {
		private String ip;
		private String host=null;
		private String status=null;
		
		public Client(String ip, String host, String status) {
			super();
			this.ip = ip;
			this.host = host;
			this.status = status;
		}
		//copy c'tor
		public Client(Client client1) {
			super();
			this.ip = client1.ip;
			this.host = client1.host;
			this.status = client1.status;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public Client(String ip) {
			super();
			this.ip = ip;
		}
		@Override
		public int hashCode() {
			return Objects.hash(host, ip, status);
		}
		
		@Override
		public boolean equals(Object obj) {
			//two clients are equal only if they have the same ID
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Client other = (Client) obj;
			return Objects.equals(ip, other.ip);
					
		}
		
		
		
		
}
