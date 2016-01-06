/**
 * Configure the Routes
 */
app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider
                // Home
                .when("/", {templateUrl: "pages/login.html", controller: "LoginController"})
                .when("/home", {templateUrl: "pages/home.html", controller: "HomeController"})
                // Pages
                .when("/createDecision", {templateUrl: "pages/createDecision.html", controller: "CreateDecisionController"})
                .when("/editDecision", {templateUrl: "pages/editDecision.html", controller: "EditDecisionController"})
                .when("/editDecision/:id", {templateUrl: "pages/editDecision.html", controller: "EditDecisionController"})
                .when("/completeDecision", {templateUrl: "pages/completeDecision.html", controller: "CompleteDecisionController"})
                .when("/relateDecision", {templateUrl: "pages/relateDecision.html", controller: "RelateDecisionController"})
                .when("/timeline", {templateUrl: "pages/timeline.html", controller: "PageCtrl"})
                .when("/showDependency", {templateUrl: "pages/showDependency.html", controller: "ShowDependencyController"})
                .when("/showGroup", {templateUrl: "pages/showGroup.html", controller: "ShowGroupController"})
                .when("/showCriteria", {templateUrl: "pages/showCriteria.html", controller: "PageCtrl"})
                .when("/userAdministration", {templateUrl: "pages/userAdministration.html", controller: "UserAdministrationController"})
                .when("/teamAdministration", {templateUrl: "pages/teamAdministration.html", controller: "TeamAdministrationController"})
                .when("/userProfile", {templateUrl: "pages/userProfile.html", controller: "UserProfileController"})
                .when("/login", {templateUrl: "pages/login.html", controller: "LoginController"})
                .when("/selectTeam", {templateUrl: "pages/selectTeam.html", controller: "TeamAdministrationController"})
                .when("/logout", {templateUrl: "pages/logout.html", controller: "LogoutController"})

                .otherwise("/login", {templateUrl: "login.html", controller: "Home"});
    }]);


app.controller('RouteController', ['$rootScope', '$cookies', function ($rootScope, $cookies) {
          $rootScope.showMenu = function(){
        	if ($cookies['TeamId'] && $cookies['Token']){
        		return true;
        	}
        	return false;
        };
        
        $rootScope.showUserMenu = function(){
        	if ($cookies['Token']){
        		return true;
        	}
        	return false;
        };
}]);
