package tddMan.Block;

public interface BlockBehaviorStrategy {
	public BlockInteractionStatus.Status OnCollision(BlockInteractiveInterface oldBlockObj, 
													 BlockInteractiveInterface newBlockObj);
}

