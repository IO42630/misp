package com.olexyn.misp.mock.exchange;


import java.util.ArrayList;
import java.util.List;

/**
 * How "exchange" mocks an exchange:<br>
 * <br>
 * An "exchange" corresponds to request & reply pair.<br>
 * - i.e. a GET & OK pair.<br>
 * Steps:<br>
 * 1. new Exchange()<br>
 * 2. set properties of .request<br>
 * 3. "send" request by using exchange.notify(); recipient.doGet();<br>
 * 4. recipient does someting with request<br>
 * - -  recipient sets proerties of .response<br>
 * - -  recipient sends reply using exchange.notify();<br>
 * 5. "receive" response by using exchange.wait();
 */
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





