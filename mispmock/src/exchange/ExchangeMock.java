package exchange;


import java.util.ArrayList;
import java.util.List;

public class ExchangeMock {

    public static List<ExchangeMock> exchangeList = new ArrayList<>();


    private static int next_id=0;
    public int id;

    public RequestMock request =new RequestMock(this);
    public ResponseMock response = new ResponseMock(this);

    public ExchangeMock(){
        id = next_id++;
        exchangeList.add(this);
    }









}





