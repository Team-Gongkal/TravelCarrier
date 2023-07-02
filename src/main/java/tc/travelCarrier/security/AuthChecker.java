package tc.travelCarrier.security;

import tc.travelCarrier.domain.*;

public class AuthChecker {

    //권한 있는지 없는지
    private boolean auth;

    
    // 위클리 수정 권한 있는지 확인하는 메소드(태그되었는지?)
    public static boolean checkWeeklyAuth(Weekly weekly, User user){
        boolean writeAuth = false;
        if(weekly.getUser() == user) writeAuth = true;
        for(Gowith gowith : weekly.getGowiths()){
            if(gowith.getUser() == user) {
                writeAuth = true;
                break;
            }
        }

        return writeAuth;
    }

    // 위클리 수정 권한 있는지 확인하는 메소드(태그되었는지?)
    public static String[] getReadAndUpdateAuth(Weekly weekly, User user){
        String[] answer = new String[3];
        OpenStatus status = weekly.getStatus();
        answer[0] = "DENIED";
        answer[1] = "DENIED";
        answer[2] = "DENIED";

        // 쓰기권한 : 무조건 글쓴이랑 태그된사람만 있음!!
        if(checkWeeklyAuth(weekly,user)) answer[1] = "GRANTED";

        // 읽기권한
        if(status == OpenStatus.ALL) answer[0] = "GRANTED";
        else if(status == OpenStatus.FOLLOW){
            for(Follower follower : weekly.getUser().getFollowers()){
                if(follower.getFollower() == user){
                    answer[0] = "GRANTED";
                    break;
                }
            }
            if(weekly.getUser() == user) answer[0] = "GRANTED";
        }else if(status == OpenStatus.ME){
            answer[0] = "DENIED";
            for(Gowith g : weekly.getGowiths()){
                if(g.getUser() == user){
                    answer[0] = "GRANTED";
                    break;
                }
            }
            if(weekly.getUser() == user) answer[0] = "GRANTED";
        }

        //본인글인지 체크
        if(weekly.getUser() == user) answer[2] = "GRANTED";
        return answer;
    }

}
