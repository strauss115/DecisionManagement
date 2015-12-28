/**
 * service for the authentification
 */
var decisionServices = angular.module('decisionServices', ['ngResource', 'ngCookies']);


decisionServices.factory('LoadGraph', ['$resource', '$cookies',
    function ($resource, $cookies) {
        return $resource(serverAddress + '/DecisionDocu/api/web/decision/getGraphAsJsonById', {}, {
        	 get: {
        		 	isArray: true,
        	        headers: {
        	            'token': $cookies['Token']
        	        }
        	    }
        });
    }]);