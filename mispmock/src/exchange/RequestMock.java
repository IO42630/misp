package exchange;

import org.springframework.mock.web.MockHttpServletRequest;

public class RequestMock extends MockHttpServletRequest  {

    public ExchangeMock exchange;

    public RequestMock(ExchangeMock exchange){
        super();
        this.exchange = exchange;
    }
}