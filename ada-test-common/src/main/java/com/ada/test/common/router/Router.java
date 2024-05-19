package com.ada.test.common.router;


public class Router {
    private Router(){throw new IllegalStateException("Router");}

    private static final String API = "/api/ada";

    public static class CompanyAPI{
        private CompanyAPI(){throw new IllegalStateException("CompanyAPI");}

        public static final String ROOT = API + "/company";
        public static final String UPDATE = "/update/{id}";
        public static final String DELETE = "/delete/{id}";
        public static final String GET = "/get/{id}";
        public static final String GET_ALL = "/get-all";
        public static final String GET_ENTITIES = "/get-entities";


    }

    public static class ApplicationAPI{
        private ApplicationAPI(){throw new IllegalStateException("ApplicationAPI");}

        public static final String ROOT = API + "/application";
        public static final String UPDATE = "/update/{id}";
        public static final String DELETE = "/delete/{id}";
        public static final String GET = "/get/{id}";
        public static final String GET_ALL = "/get-all";

    }

    public static class VersionAPI{
        private VersionAPI(){throw new IllegalStateException("VersionAPI");}

        public static final String ROOT = API + "/version";
        public static final String UPDATE = "/update/{id}";
        public static final String DELETE = "/delete/{id}";
        public static final String GET = "/get/{id}";
        public static final String GET_ALL = "/get-all";

    }

    public static class CompanyVersionAPI{
        private CompanyVersionAPI(){throw new IllegalStateException("CompanyVersionAPI");}

        public static final String ROOT = API + "/company-version";
        public static final String UPDATE = "/update/{id}";
        public static final String DELETE = "/delete/{id}";
        public static final String GET = "/get/{id}";
        public static final String GET_ALL = "/get-all";
        public static final String MASSIVE = "/massive";

    }

    public static class CalculatorAPI{
        private CalculatorAPI(){throw new IllegalStateException("CalculatorAPI");}

        public static final String ROOT = API + "/calculator";
        public static final String ADD = "/add";
        public static final String SUBTRACT = "/subtract";
        public static final String MULTIPLY = "/multiply";
        public static final String DIVIDE = "/divide";
        public static final String POWER = "/power";
        public static final String ROOTS = "/roots";

    }

    public static class WordsAPI{
        private WordsAPI(){throw new IllegalStateException("WordsAPI");}

        public static final String ROOT = API + "/words";
        public static final String FIND = "/find-word";

    }
}
