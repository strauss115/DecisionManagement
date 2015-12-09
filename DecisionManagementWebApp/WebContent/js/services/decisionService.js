/**
 * service for the authentification
 */
var decisionServices = angular.module('decisionServices', ['ngResource']);


decisionServices.factory('LoadGraph', ['$resource',
    function ($resource) {
        return $resource(serverAddress + '/DecisionManagement/rest/decision/byId/', {}, {
            query: {method: 'GET', isArray: false}
        });
    }]);





