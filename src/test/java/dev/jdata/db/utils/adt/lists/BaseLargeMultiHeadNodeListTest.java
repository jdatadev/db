package dev.jdata.db.utils.adt.lists;

abstract class BaseLargeMultiHeadNodeListTest extends BaseLongNodeListTest {

    static final class TestList {

        private long headNode;
        private long tailNode;

        long getHeadNode() {
            return headNode;
        }

        void setHeadNode(long headNode) {
            this.headNode = headNode;
        }

        long getTailNode() {
            return tailNode;
        }

        void setTailNode(long tailNode) {
            this.tailNode = tailNode;
        }
    }
}
