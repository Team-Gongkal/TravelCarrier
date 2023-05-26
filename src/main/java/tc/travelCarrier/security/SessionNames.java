package tc.travelCarrier.security;

public enum SessionNames {
    LOGIN;

    public String getKey() {
        return name(); // 상수의 이름을 그대로 반환
    }
}
