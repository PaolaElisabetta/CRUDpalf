
import javax.servlet.Filter;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pccube.crudtest.controller.TestController;
import com.pccube.crudtest.entities.User;

public class UserControllerUnitTest {

	 private MockMvc mockMvc;

	    @Mock
	    private User user;

	    @InjectMocks
	    private TestController testController;

	    @Before
	    public void init(){
	        MockitoAnnotations.initMocks(this);
	        mockMvc = MockMvcBuilders
	                .standaloneSetup(testController)
	                .build();
	    }


	
}
