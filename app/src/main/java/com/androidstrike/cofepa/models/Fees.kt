package com.androidstrike.cofepa.models

class Fees {
    lateinit var acceptance: String
    lateinit var accommodation: String
    lateinit var exams: String
    lateinit var hazard: String
    lateinit var libraryEquipment: String
    lateinit var medicalTreatment: String
    lateinit var psa: String
    lateinit var portal: String
    lateinit var security: String
    lateinit var sports: String
    lateinit var total: String
    lateinit var tuition: String

    constructor()
    constructor(
        acceptance: String,
        accommodation: String,
        exams: String,
        hazard: String,
        libraryEquipment: String,
        medicalTreatment: String,
        psa: String,
        portal: String,
        security: String,
        sports: String,
        total: String,
        tuition: String
    ) {
        this.acceptance = acceptance
        this.accommodation = accommodation
        this.exams = exams
        this.hazard = hazard
        this.libraryEquipment = libraryEquipment
        this.medicalTreatment = medicalTreatment
        this.psa = psa
        this.portal = portal
        this.security = security
        this.sports = sports
        this.total = total
        this.tuition = tuition
    }


}