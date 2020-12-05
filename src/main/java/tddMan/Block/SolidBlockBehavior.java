package tddMan.Block;

public class SolidBlockBehavior implements BlockBehaviorStrategy {
	public void OnCollision() {
		
	}

	public void OnCollision(BlockInteractiveInterface bObj) {
		return;
	}

	public BlockInteractionStatus.Status OnCollision(BlockInteractiveInterface oldBlockObj, BlockInteractiveInterface newBlockObj){
		return BlockInteractionStatus.Status.SOLID_COLLISION;
	}
}
