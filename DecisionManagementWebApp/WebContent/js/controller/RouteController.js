/**
 * Configure the Routes
 */
app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider
                // Home
                .when("/", {templateUrl: "pages/login.html", controller: "LoginController"})
                .when("/home", {templateUrl: "pages/home.html", controller: "Home"})
                // Pages
                .when("/createDecision", {templateUrl: "pages/createDecision.html", controller: "CreateDecisionController"})
                .when("/editDecision", {templateUrl: "pages/editDecision.html", controller: "EditDecisionController"})
                .when("/completeDecision", {templateUrl: "pages/completeDecision.html", controller: "PageCtrl"})
                .when("/relateDecision", {templateUrl: "pages/relateDecision.html", controller: "RelateDecisionController"})
                .when("/timeline", {templateUrl: "pages/timeline.html", controller: "PageCtrl"})
                .when("/showDependency", {templateUrl: "pages/showDependency.html", controller: "ShowDependencyController"})
                .when("/showGroup", {templateUrl: "pages/showGroup.html", controller: "ShowGroupController"})
                .when("/showCriteria", {templateUrl: "pages/showCriteria.html", controller: "PageCtrl"})
                .when("/userAdministration", {templateUrl: "pages/userAdministration.html", controller: "UserAdministrationController"})
                .when("/teamAdministration", {templateUrl: "pages/teamAdministration.html", controller: "PageCtrl"})
                .when("/userProfile", {templateUrl: "pages/userProfile.html", controller: "PageCtrl"}, {resolve: {
                        access: ["Access", function (Access) {
                                return Access.isAnonymous();
                            }]}
                })
                .when("/login", {templateUrl: "pages/login.html", controller: "LoginController"})


                .otherwise("/login", {templateUrl: "login.html", controller: "Home"});
    }]);
