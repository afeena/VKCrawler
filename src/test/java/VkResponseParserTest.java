import org.junit.Assert;
import org.junit.Test;
import ru.afeena.crawler.VkResponseParser;

import java.util.ArrayList;

public class VkResponseParserTest {

	@Test

	public void wallParseTextTest() {
		//arrange
		String response = "{\"response\":[60,{\"id\":475,\"from_id\":8,\"to_id\":8,\"date\":1412280264,\"post_type\":\"post\",\"text\":\"Test case text\",\"attachment\":{\"type\":\"photo\",\"photo\":{\"pid\":340690046,\"aid\":-7,\"owner_id\":8,\"src\":\"http:\\/\\/cs619428.vk.me\\/v619428008\\/1a8e1\\/4eW54Zthbzc.jpg\",\"src_big\":\"http:\\/\\/cs619428.vk.me\\/v619428008\\/1a8e2\\/FVrRhTXn2w8.jpg\",\"src_small\":\"http:\\/\\/cs619428.vk.me\\/v619428008\\/1a8e0\\/pi5fAxU80XM.jpg\",\"width\":200,\"height\":200,\"text\":\"\",\"created\":1412280264,\"post_id\":475,\"access_key\":\"b740e68f5a2a859b32\"}},\"attachments\":[{\"type\":\"photo\",\"photo\":{\"pid\":340690046,\"aid\":-7,\"owner_id\":8,\"src\":\"http:\\/\\/cs619428.vk.me\\/v619428008\\/1a8e1\\/4eW54Zthbzc.jpg\",\"src_big\":\"http:\\/\\/cs619428.vk.me\\/v619428008\\/1a8e2\\/FVrRhTXn2w8.jpg\",\"src_small\":\"http:\\/\\/cs619428.vk.me\\/v619428008\\/1a8e0\\/pi5fAxU80XM.jpg\",\"width\":200,\"height\":200,\"text\":\"\",\"created\":1412280264,\"post_id\":475,\"access_key\":\"b740e68f5a2a859b32\"}}],\"comments\":{\"count\":0},\"likes\":{\"count\":15},\"reposts\":{\"count\":2}}]}";
		ArrayList<String> expected_result = new ArrayList<String>();
		expected_result.add("Test case text");

		//act
		VkResponseParser responseParser = new VkResponseParser();
		ArrayList<String> result = responseParser.parseWall(response, 1);

		//assert

		Assert.assertEquals(expected_result, result);


	}
}
