package exchange;


import org.springframework.mock.web.MockHttpServletResponse;

public class ResponseMock extends MockHttpServletResponse {

    public ExchangeMock exchange;

    public ResponseMock(ExchangeMock exchange){
        super();
        this.exchange = exchange;
    }



}
