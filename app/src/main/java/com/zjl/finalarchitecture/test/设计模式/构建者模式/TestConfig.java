package com.zjl.finalarchitecture.test.设计模式.构建者模式;

/**
 * @author Xiaoc
 * @since 2023-02-19
 **/
public class TestConfig {

    private boolean enable1;

    private boolean enable2;

    private boolean enable3;

    private boolean enable4;

    public void setEnable1(boolean enable1) {
        this.enable1 = enable1;
    }

    public void setEnable2(boolean enable2) {
        this.enable2 = enable2;
    }

    public void setEnable3(boolean enable3) {
        this.enable3 = enable3;
    }

    public void setEnable4(boolean enable4) {
        this.enable4 = enable4;
    }

    private TestConfig(boolean enable1, boolean enable2, boolean enable3, boolean enable4) {
        this.enable1 = enable1;
        this.enable2 = enable2;
        this.enable3 = enable3;
        this.enable4 = enable4;
    }

    public static class Builder{

        private boolean enable1Builder;

        private boolean enable2Builder;

        private boolean enable3Builder;

        private boolean enable4Builder;

        public Builder setEnable1Builder(boolean enable1Builder) {
            this.enable1Builder = enable1Builder;
            return this;
        }

        public Builder setEnable2Builder(boolean enable2Builder) {
            this.enable2Builder = enable2Builder;
            return this;
        }

        public Builder setEnable3Builder(boolean enable3Builder) {
            this.enable3Builder = enable3Builder;
            return this;
        }

        public Builder setEnable4Builder(boolean enable4Builder) {
            this.enable4Builder = enable4Builder;
            return this;
        }

        public TestConfig build(){
            return new TestConfig(enable1Builder, enable2Builder, enable3Builder, enable4Builder);
        }
    }
}
