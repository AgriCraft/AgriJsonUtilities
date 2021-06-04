package com.agricraft.agrijsonutilities.github;

import com.agricraft.agrijsonutilities.util.AgriJsonType;

public interface FileType {
    boolean isDir();

    boolean isJson();

    AgriJsonType getType();

    FileType INVALID = new FileType() {
        @Override
        public boolean isDir() {
            return false;
        }

        @Override
        public boolean isJson() {
            return false;
        }

        @Override
        public AgriJsonType getType() {
            throw new IllegalStateException("Invalid file type does not have a json type");
        }
    };

    FileType DIR = new FileType() {
        @Override
        public boolean isDir() {
            return true;
        }

        @Override
        public boolean isJson() {
            return false;
        }

        @Override
        public AgriJsonType getType() {
            throw new IllegalStateException("Directory file type does not have a json type");
        }
    };

    FileType JSON_PLANT = new FileType() {
        @Override
        public boolean isDir() {
            return false;
        }

        @Override
        public boolean isJson() {
            return true;
        }

        @Override
        public AgriJsonType getType() {
            return AgriJsonType.PLANT;
        }
    };

    FileType JSON_SOIL = new FileType() {
        @Override
        public boolean isDir() {
            return false;
        }

        @Override
        public boolean isJson() {
            return true;
        }

        @Override
        public AgriJsonType getType() {
            return AgriJsonType.SOIL;
        }
    };

    FileType JSON_MUTATION = new FileType() {
        @Override
        public boolean isDir() {
            return false;
        }

        @Override
        public boolean isJson() {
            return true;
        }

        @Override
        public AgriJsonType getType() {
            return AgriJsonType.MUTATION;
        }
    };

    FileType JSON_WEED = new FileType() {
        @Override
        public boolean isDir() {
            return false;
        }

        @Override
        public boolean isJson() {
            return true;
        }

        @Override
        public AgriJsonType getType() {
            return AgriJsonType.WEED;
        }
    };
}
