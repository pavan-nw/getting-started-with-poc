package com.example.demo;

import com.tailf.jnc.*;

import java.io.IOException;

import javax.swing.*;

public class NEClient {

	private static final String emsUserName = "emsadmin";
	// private static final String ip = "10.220.165.105";
	// private static final String ip = "172.16.0.194"; // Test team node
	private static final String ip = "10.220.165.88"; // Pawan node

	private Device device;
	private Device notificationDevice;
	private DeviceUser duser;
	private DeviceUser notificationUser;
	
	

	NEClient() {

		duser = new DeviceUser(emsUserName, "emsadmin", "Infinera#1");
		notificationUser = new DeviceUser(emsUserName, "emsadmin", "Infinera#1");
		device = new Device(ip, duser, ip, 830);
		notificationDevice = new Device(ip + "_notify", notificationUser, ip, 830);
		this.init();
	}

	private void init() {

		try {
			device.connect(emsUserName);
			device.newSession(new DefaultIOSubscriber(this.device.name), "cfg");

//			notificationDevice.connect(emsUserName);
//			notificationDevice.newSession(new DefaultIOSubscriber(this.device.name), "notification");
//			subscribeToAlarmNotifications();
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		} catch (JNCException e) {
			e.printStackTrace();
			System.err.println("Can't authenticate " + e);
			System.exit(1);
		}
	}

	private void subscribeToAlarmNotifications() {
		try {
			notificationDevice.getSession("notification").createSubscription("Alarm");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JNCException e) {
			e.printStackTrace();
		}
	}

	NodeSet editConfig(Element config) throws IOException, JNCException {
		return editConfig(device, config);
	}

	private NodeSet editConfigElement(Element element, String moName, String name, String value)
			throws IOException, JNCException {
		element.getChild(moName).getChild(name).setValue(value);
		return editConfig(device, element);

	}

	private NodeSet editConfig(Device d, Element config) throws IOException, JNCException {
		d.getSession("cfg").editConfig(config);
		// Inspect the updated RUNNING configuration
		return getConfig(d);
	}

	private NodeSet getConfig(Device d) throws IOException, JNCException {
		NetconfSession session = d.getSession("cfg");

		NodeSet reply = session.getConfig(NetconfSession.RUNNING);
		Element reply2 = session.rpc("<rpc xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"1\">\n"
				+ "    <get>\n" + "      <filter xmlns=\"com:infinera:alarm\">\n" + "          <asap/>\n"
				+ "      </filter>\n" + "    </get>\n" + "  </rpc>");
		return reply;
	}

	private Element getAlarmsViaRpc(Device d) throws IOException, JNCException {
		NetconfSession session = d.getSession("cfg");

		Element reply = session.rpc("<rpc xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"1\">\n"
				+ "    <get>\n" + "      <filter xmlns=\"com:infinera:alarm\">\n" + "          <asap/>\n"
				+ "      </filter>\n" + "    </get>\n" + "  </rpc>");
		return reply;
	}

	public Element getMeConfigViaRpc() throws IOException, JNCException {
		Device d = device;
		NetconfSession session = d.getSession("cfg");

		Element reply = session.rpc("<rpc xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"1\">\n"
				+ "      <get-config>\n" + "          <source>\n" + "          <running/>\n" + "          </source>\n"
				+ "      <filter xmlns=\"com:infinera:me\">\n" + "          <ManagedElement/>\n" + "      </filter>\n"
				+ "    </get-config>\n" + "  </rpc>");
		return reply;
	}

	public NodeSet getConfig() throws IOException, JNCException {
		return getConfig(device);
	}

	/**
	 * Gets the first configuration element in configs with specified name.
	 *
	 * @param configs
	 *            Set of device configuration data.
	 * @param name
	 *            The identifier of the configuration to select
	 * @return First configuration with matching name, or null if none present.
	 */
	public static Element getConfig(NodeSet configs, String name) {
		Element config = configs.first();
		if (!config.name.equals(name)) {
			config = null;
			for (Element elem : configs) {
				if (elem.name.equals(name)) {
					config = elem;
				}
			}
		}
		return config;
	}

	/**
	 * @param args
	 *            Ignored
	 * @throws JNCException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, JNCException {
		NEClient neClient = new NEClient();
		// 1 . start listening Notifications from NE
		startNotificationListener(neClient);

		// 2. get All config of NE
		NodeSet nodeSet = neClient.getConfig(neClient.device);
		// printAllNodeSet(nodeSet);

		// 3. Filter and get Chassi from all NE config
		Element chassis = getConfig(nodeSet, "chassis");
		printElements(chassis);

		// 4. update chassis label

		nodeSet = neClient.editConfigElement(chassis, "chassis", "Label", "old_________CX_NODE_Label");
		// nodeSet = neClient.editConfigElement(chassis, "chassis",
		// "AdministrativeState", "Locked");
		chassis = getConfig(nodeSet, "chassis");
		printElements(chassis);

		// 5. filter Equipments
		System.out.println("\n\nAll Equipments::::::::::::::::::::::::::::::::::::");
		Element equipements = getConfig(nodeSet, "equipments");
		printElements(equipements);

		// System.out.println("\n\nAll
		// Alarams::::::::::::::::::::::::::::::::::::" );
		// Element alarms = neClient.getAlarmsViaRpc(neClient.device);
		// printElements(alarms);

		System.out.println("\n\nME Config::::::::::::::::::::::::::::::::::::");
		Element meconfig = neClient.getMeConfigViaRpc();
		printElements(meconfig);

		// Cleanup
		neClient.device.close();
	}

	private static void startNotificationListener(final NEClient neClient) {
		Thread notificationListenerThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					System.out.println("Listening Notifications :::::::::::::::::::::::::::::::::::::::::::");
					Element element = null;
					try {
						element = neClient.notificationDevice.getSession("notification").receiveNotification();
						// element =
						// neClient.device.getSession("cfg").receiveNotification();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JNCException e) {
						e.printStackTrace();
					}
					System.out.println(element.toXMLString());

				}
			}
		});
		notificationListenerThread.start();
	}

	private static void printElements(Element element) {
		if (element.getChildren() != null) {

			for (Element element1 : element.getChildren()) {
				printElements(element1);

			}
			System.out.println("\n");
		} else {
			System.out.println(element.name + " = " + element.getValue());
		}
	}

	private static void printAllNodeSet(NodeSet nodeSet) {

		for (Element element : nodeSet) {
			System.out.println("Attrribute ============================================= " + element.name);
			printElements(element);
		}
	}
}
