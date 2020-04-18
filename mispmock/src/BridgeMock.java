public class BridgeMock extends BridgeServlet {

    private MockSet mockSet;

    public BridgeMock(MockSet mockSet){
        super();
        mockSet.bridgeMock=this;
        this.mockSet = mockSet;
    }




}
