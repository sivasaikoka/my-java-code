package com.dipl.abha.config;

public class TenantRoleContext {

	private static final ThreadLocal<String> NDHM_CLIENT_ID = new InheritableThreadLocal<>();
	private static final ThreadLocal<String> NDHM_CLIENT_SECRETE_KEY = new InheritableThreadLocal<>();

	private TenantRoleContext() {
	}

	public static void setNdhmClientId(String clientId) {
		NDHM_CLIENT_ID.set(clientId);
	}

	public static String getNdhmClientId() {
		return NDHM_CLIENT_ID.get();
	}

	public static void setNdhmClientSecreteKey(String clientSecrete) {
		NDHM_CLIENT_SECRETE_KEY.set(clientSecrete);
	}

	public static String getNdhmClientSecreteKey() {
		return NDHM_CLIENT_SECRETE_KEY.get();
	}

	public static void clear() {
		NDHM_CLIENT_ID.remove();
		NDHM_CLIENT_SECRETE_KEY.remove();
	}

}
