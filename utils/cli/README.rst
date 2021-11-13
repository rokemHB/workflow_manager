====================
Workflow Manager API
====================

Command Line Interface
----------------------

Diese Datei beschreibt die Verwendung des Workflow Manager Command Line Interfaces (CLI). Durch dieses lassen
sich grundlegende Operationen des Workflow Managers ohne die Verwendung der Weboberfläche ausführen.

In einer ersten Version sind folgende Operationen möglich:

- Einträge anzeigen
    - Für Klassen: User, ProcessChain, Procedure, Job
- Auswertung erstellen
    - Für Klasse Procedure (ProcessStep bzw. Prozesschritte)
- Einträge bearbeiten
    - Für Klasse Job (Hierbei das ändern der Priorität eines Auftrages)
- Einträge erstellen
    - Für Klasse User

Installation Python
^^^^^^^^^^^^^^^^^^^

Zur Verwendung des Workflow Manager CLI müssen auf dem Anwendungssystem (Client) zunächst die erforderlichen
Programme installiert werden. Da das Skript in der Sprache Python geschrieben wurde muss zunächst der Python
Interpreter auf dem System installiert werden. Da sich die Installation für verschiedene Betriebssysteme
unterscheidet, kann eine Installationsanleitung auf der Seite python.org_ gefunden werden.

.. _python.org: http://www.python.org/

.. hint::

    Für die Verwendung des Workflow Manager CLI wird eine Version > 3.5 benötigt.

Intallation erforderliche Pakete
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Zudem wird zur Benutzung des Workflow Managet CLI das Python Paket 'requests' benötigt.
Dieses kann über den Python Package Management Tool ``pip install packages (pip)`` installiert werden.

Hierzu kann entweder:

1. Das Paket manuell über pip installiert werden (Kommando: ``pip install requests``)
2. Das benötigte Paket über die requirements.txt installiert werden: (Kommando: ``pip install -r requirements.txt``)

Verwendung
----------

Dieser Abschnitt beschreibt die Verwendung des Workflow Manager CLI.
Das Programm kann durch den Befehl ``python /pfad/zur/kcb.py`` ausgeführt werden.

Eine zusätzliche Übersicht über die verfügbaren Befehle kann durch das Kommando ``python /pfad/zur/kcb.py -h``
angezeigt werden.

Generierung der Basiskonfiguration
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Zum Ausführen des Workflow Manager CLI wird eine Konfigurationsdatei benötigt. Diese befindet sich im Standardfall
im selben Ordner wie die ``kcb.py`` Datei. Vor der ersten Ausführung muss eine Konfigurationsdatei für das
Workflow Manager CLI generiert werden. Hierzu kann der Befehl: ``python /pfad/zur/kcb.py --generate-config``
verwendet werden. Dieser erzeugt in genanntem Ordner eine config.ini Datei, die anschließend mit gültigen
Werten befüllt werden muss.

Beispielhafte Konfigurationsdatei:

.. code-block::

    [DEFAULT]
    base_url = http://example.com/path/to/deployment

    [CREDENTIALS]
    username = <username>
    password = <password>

Auflistung von Einträgen
^^^^^^^^^^^^^^^^^^^^^^^^

Durch den Befehl ``python /pfad/zur/kcb.py -l`` können die Entitäten verschiedener Klassen aufgelistet werden.
Die für diese Operation verfügbaren Klassen sind:

1. User
2. ProcessChain (Prozesskette)
3. Procedure (Instanz eines Prozessschrittes)
4. Job (Instanz einer Prozesskette)

.. hint::

   Die Ausgabe der Daten erfolgt im JSON-Format



Erzeugung von Einträgen
^^^^^^^^^^^^^^^^^^^^^^^

Durch den Befehl ``python /pfad/zur/kcb.py -a`` lassen sich Entitäten für verschiedene Klassen erstellen.
Momentan wird diese Operation ausschließlich für die Klasse User unterstützt.


Auswertung von Daten
^^^^^^^^^^^^^^^^^^^^

Durch den Befehl ``python /pfad/zur/kcb.py -e`` lassen sich Auswertungen für verschiedene Klassen erstellen.
Diese Opeeration ist momentan für folgende Klassen verfügbar:

1. Procedure / Prozessschritte

Die Auswertung zeigt für jeden Prozessschritt die durchschnittliche Bearbeitungszeit aller bereits durchgeführter
Instanzen dieses Prozessschrittes.

REST Endpunkte
--------------

Der folgende Abschnitt beschreibt die REST Endpunkte, über die die Daten des Workflow Managers angezeigt,
bearbeitet und erzeugt werden können. Hierzu muss zur Authentifizierung die HttpBasicAuth Methode verwendet werden.

Grundsätzlich sind auf fast allen Klassen die CRUD (Create, Read, Update, Delete) Operationen für die REST-API
freigeschaltet.

Hierbei sind die Operationen jeweils durch den jeweiligen HTTP-Request auf den jeweiligen Endpunkt ansprechbar.

Diese sind:

1. GET-Request: Anzeige der Daten des jeweiligen Endpunktes
2. POST-Request: Erstellen von Daten des jeweiligen Endpunktes
3. PUT-Request: Update von bestehenden Datensätzen des jeweiligen des jeweiligen Endpunktes
4. DELETE-Request: Löschen von bestehenden Datensätzen des jeweiligen Endpunktes


.. hint::

   Im folgenden wird die Bezeichnung ``BASE_URL`` verwendet. Diese bezeichnet die URL des Wildfly Servers inklusive des Pfades zum jeweiligen Deployments. Für einen Server mit der URL https://www.example.com und einem Deployment mit dem Namen workflow-manger ergibt sich die BASE_URL: ``https://www.example.com/workflow-manager``.


Benutzer
^^^^^^^^

Der Benutzerendpunkt ist unter der URL ``{BASE_URL}/api/v1/user`` erreichbar.

1. Benutzer anzeigen:
    Durch einen GET-Resquest auf den Benutzerendpunkt können die verfügbaren Benutzer abgefragt werden.

2. Informationen zu Benutzer anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/user/{id}`` können Informationen zu einem User mit der gegebenen ID
    abgefragt werden.

3. Benutzer hinzufügen:
    Durch einen POST-Request auf den Benutzerendpunkt können neue Benutzer erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit folgende Informationen enthalten:

    .. code-block::

       {
        "username": "admin",
        "firstName": "Armin",
        "lastName": "Admin",
        "email": "admin@example.com",
        "roles": [
          "ADMIN"
        ]
      },

4. Benutzer bearbeiten:
    Wie beim hinzufügen von neuen Benutzern muss auch beim bearbeiten von bestehenden Benutzern gültiges
    JSON Format mit den folgenden Informationen mittels PUT-Request an den User Endpunkt gesendet werden.

     .. code-block::

       {
        "id": "2c720356-2c23-419a-b807-d3d36b9de528",
        "username": "admin",
        "firstName": "Armin",
        "lastName": "Admin",
        "email": "admin@example.com",
        "roles": [
          "ADMIN"
        ]
      },

5. Benutzer löschen:
    Zum löschen eines Benutzers muss ein DELETE-Request an den User Endpunkt gesendet werden. Hierbei
    muss entweder der Benutzername, die E-Mail oder die ID des zu löschenden Benutzers im Request Body
    als JSON angegeben werden.

Assembly
^^^^^^^^

Der Assembly Endpunkt ist unter der URL ``{BASE_URL}/api/v1/sample`` erreichbar.

1. Assembly anzeigen:
    Durch einen GET-Resquest auf den Assembly Endpunkt können die verfügbaren Assemblies abgefragt werden.

2. Informationen zu Assembly anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/sample/{id}`` können Informationen zu einer Probe mit der gegebenen ID
    abgefragt werden.

3. Assemblies hinzufügen:
    Durch einen POST-Request auf den Assembly Endpunkt können neue Assemblies erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit folgende Informationen enthalten:

    .. code-block::

      {
        "id": "252f0f97-d89a-48e0-aff4-2a19ec6911d4",
        "assemblyID": "A01.0.0",
        "sampleCount": 10,
        "alloy": "Stahl",
        "modifications": [],
        "positionAtCarrier": null,
        "comment": null,
        "carriers": [
          {
            "id": "9df84d1c-a6c4-4461-a2bd-54418b13602f",
            "carrierID": "Carrier 00",
            "carrierType": {
              "id": "24279045-a421-4aea-9b97-65b0407c7452",
              "name": "Glas"
            },
            "location": {
              "position": "Lager"
            }
          }
        ]
        },


4. Assembly bearbeiten:
    Wie beim hinzufügen von neuen Assemblies muss auch beim bearbeiten von bestehenden Assemblies gültiges
    JSON mit den folgenden Informationen mittels PUT-Request an den Assembly Endpunkt gesendet werden.

     .. code-block::

       {
        "id": "252f0f97-d89a-48e0-aff4-2a19ec6911d4",
        "assemblyID": "A01.0.0",
        "sampleCount": 10,
        "alloy": "Stahl",
        "modifications": [],
        "positionAtCarrier": null,
        "comment": null,
        "carriers": [
          {
            "id": "9df84d1c-a6c4-4461-a2bd-54418b13602f",
            "carrierID": "Carrier 00",
            "carrierType": {
              "id": "24279045-a421-4aea-9b97-65b0407c7452",
              "name": "Glas"
            },
            "location": {
              "position": "Lager"
            }
          }
        ]
      },

5. Assembly löschen:
    Zum löschen einer Assembly muss ein DELETE-Request an den Assembly Endpunkt gesendet werden. Dieser muss
    die ID der zu löschenden Assembly als JSON enthalten.

    .. code-block::

      {
        "id": "252f0f97-d89a-48e0-aff4-2a19ec6911d4",
      }

Cardinal Value
^^^^^^^^^^^^^^

Der Cardinal Value Endpunkt ist unter der URL ``{BASE_URL}/api/v1/cardinalvalue`` erreichbar.

1. Cardinal Value anzeigen:
    Durch einen GET-Resquest auf den Cardinal Value Endpunkt können die vorhandenen Cardinal Values abgefragt werden.

2. Informationen zu Cardinal Value anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/sample/{id}`` können Informationen zu einem Cardinal Value mit der gegebenen ID
    abgefragt werden.

3. Cardinal Value hinzufügen:
    Durch einen POST-Request auf den Cardinal Value Endpunkt können neue Cardinal Values erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit folgende Informationen enthalten:

    .. code-block::

      {
        "unit": "°C",
        "value": "10",
        "parameter": {
          "id": "4bfca04c-ec2e-49e5-8db9-1eb09f7a40ca",
          "field": "Temperatur"
        }
      }


4. Cardinal Value bearbeiten:
    Wie beim hinzufügen von neuen Cardinal Values muss auch beim bearbeiten von bestehenden Cardinal Values gültiges
    JSON mit den folgenden Informationen mittels PUT-Request an den Cardinal Value Endpunkt gesendet werden.

     .. code-block::

      {
        "unit": "°C",
        "id": "100e58b3-7601-401e-aa13-4dd713d976c7",
        "value": "20",
        "parameter": {
          "id": "4bfca04c-ec2e-49e5-8db9-1eb09f7a40ca",
          "field": "Temperatur"
        }
      }

5. Cardinal Value löschen:
    Zum löschen eines Cardinal Values muss ein DELETE-Request an den Cardinal Value Endpunkt gesendet werden. Dieser muss
    die ID des zu löschenden Cardinal Values als JSON enthalten.

    .. code-block::

      {
        "id": "100e58b3-7601-401e-aa13-4dd713d976c7",
      }

Carrier
^^^^^^^

Der Carrier Endpunkt ist unter der URL ``{BASE_URL}/api/v1/carrier`` erreichbar.

1. Carrier anzeigen:
    Durch einen GET-Resquest auf den Carrier Endpunkt können die vorhandenen Carrier abgefragt werden.

2. Informationen zu Carrier anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/carrier/{id}`` können Informationen zu einem Carrier mit der gegebenen ID
    abgefragt werden.

3. Carrier hinzufügen:
    Durch einen POST-Request auf den Carrier Endpunkt können neue Carrier erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit folgende Informationen enthalten:

    .. code-block::

      {
        "carrierID": "Carrier 00",
        "carrierType": {
          "id": "24279045-a421-4aea-9b97-65b0407c7452",
          "name": "Glas"
        },
        "location": {
          "position": "Lager"
        }
      },


4. Carrier bearbeiten:
    Wie beim hinzufügen von neuen Carrier muss auch beim bearbeiten von bestehenden Carriers gültiges
    JSON mit den folgenden Informationen mittels PUT-Request an den Carrier Endpunkt gesendet werden.

     .. code-block::

       {
        "id": "9df84d1c-a6c4-4461-a2bd-54418b13602f",
        "carrierID": "Carrier 00",
        "carrierType": {
          "id": "24279045-a421-4aea-9b97-65b0407c7452",
          "name": "Steckbrett"
        },
        "location": {
          "position": "Lager"
        }
      },

5. Carrier löschen:
    Zum löschen einer Carriers muss ein DELETE-Request an den Carrier Endpunkt gesendet werden. Dieser muss
    die ID der zu löschenden Carrier als JSON enthalten.

    .. code-block::

      {
        "id": "252f0f97-d89a-48e0-aff4-2a19ec6911d4",
      }

CarrierType
^^^^^^^^^^^

Der CarrierType Endpunkt ist unter der URL ``{BASE_URL}/api/v1/carrier/type`` erreichbar.

1. CarrierType anzeigen:
    Durch einen GET-Resquest auf den CarrierType Endpunkt können die vorhandenen CarrierTypes abgefragt werden.

2. Informationen zu CarrierTypes anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/carrier/type/{id}`` können Informationen zu einem CarrierType mit der gegebenen ID
    abgefragt werden.

3. CarrierType hinzufügen:
    Durch einen POST-Request auf den CarrierType Endpunkt können neue CarrierTypes erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit folgende Informationen enthalten:

    .. code-block::

      {
        "name": "Glas"
      },


4. CarrierType bearbeiten:
    Wie beim hinzufügen von neuen CarrierTypes muss auch beim bearbeiten von bestehenden CarrierTypes gültiges
    JSON mit den folgenden Informationen mittels PUT-Request an den CarrierType Endpunkt gesendet werden.

     .. code-block::

       {
        "id": "24279045-a421-4aea-9b97-65b0407c7452",
        "name": "Glas"
       },

5. CarrierType löschen:
    Zum löschen einer CarrierType muss ein DELETE-Request an den CarrierType Endpunkt gesendet werden. Dieser muss
    die ID der zu löschenden CarrierType als JSON enthalten.

    .. code-block::

      {
        "id": "24279045-a421-4aea-9b97-65b0407c7452",
      }

GlobalConfig
^^^^^^^^^^^^

Der GlobalConfig Endpunkt ist unter der URL ``{BASE_URL}/api/v1/globalconfig`` erreichbar.

1. GlobalConfig anzeigen:
    Durch einen GET-Resquest auf den GlobalConfig Endpunkt können die vorhandenen GlobalConfig Einträge abgefragt werden.

3. GlobalConfig Eintrag hinzufügen:
    Durch einen POST-Request auf den GlobalConfig Endpunkt können neue GlobalConfig Einträge erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit folgende Informationen enthalten:

    .. code-block::

      {
        "key": "oldActiveJob",
        "value": "4786"
      }


4. GlobalConfig Eintrag bearbeiten:
    Wie beim hinzufügen von neuen GlobalConfig Einträgen muss auch beim bearbeiten von bestehenden GlobalConfig einträgen gültiges
    JSON mit den folgenden Informationen mittels PUT-Request an den GlobalConfig Endpunkt gesendet werden.

     .. code-block::

       {
        "key": "oldActiveJob",
        "value": "4786"
        }


Job
^^^

Der Job Endpunkt ist unter der URL ``{BASE_URL}/api/v1/job`` erreichbar.

1. Jobs anzeigen:
    Durch einen GET-Resquest auf den Job Endpunkt können die vorhandenen Jobs abgefragt werden.

2. Informationen zu Job anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/job/{id}`` können Informationen zu einem Job mit der gegebenen ID
    abgefragt werden.

3. Job hinzufügen:
    Durch einen POST-Request auf den Job Endpunkt können neue Jobs erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit einer Beschreibung des Jobs enthalten.
    Beispiele hierfür können über den GET-Endpunkt erhalten werden.


4. Job bearbeiten:
    Wie beim hinzufügen von neuen Jobs muss auch beim bearbeiten von bestehenden Jobs gültiges
    JSON mittels PUT-Request an den Job Endpunkt gesendet werden. Beispiele hierfür können ebenfalls
    über den GET-Endpunkt abgefragt werden.


5. Job löschen:
    Zum löschen eines Jobs muss ein DELETE-Request an den Job Endpunkt gesendet werden. Dieser muss
    die ID des zu löschenden Jobs als JSON enthalten.

    .. code-block::

      {
        "id": "9c915fd5-c654-4e74-b7bd-bf7fc15ae01e",
      }

Parameter
^^^^^^^^^

Der Parameter Endpunkt ist unter der URL ``{BASE_URL}/api/v1/parameter`` erreichbar.

1. Parameter anzeigen:
    Durch einen GET-Resquest auf den Parameter Endpunkt können die vorhandenen Parameter abgefragt werden.

2. Informationen zu Parameter anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/parameter{id}`` können Informationen zu einem Parameter mit der gegebenen ID
    abgefragt werden.

3. Paramter hinzufügen:
    Durch einen POST-Request auf den Parameter Endpunkt können neue Parameter erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit folgende Informationen enthalten:

    .. code-block::

      {
        "field": "Temperatur"
      },


4. Paramter bearbeiten:
    Wie beim hinzufügen von neuen Parametern muss auch beim bearbeiten von bestehenden Paramtern gültiges
    JSON mit den folgenden Informationen mittels PUT-Request an den Paramter Endpunkt gesendet werden.

     .. code-block::

       {
        "id": "4bfca04c-ec2e-49e5-8db9-1eb09f7a40ca",
        "field": "Druck"
       },

5. Parameter löschen:
    Zum löschen eines Parameters muss ein DELETE-Request an den Parameter Endpunkt gesendet werden. Dieser muss
    die ID des zu löschenden Paramter als JSON enthalten.

    .. code-block::

      {
        "id": "4bfca04c-ec2e-49e5-8db9-1eb09f7a40ca",
      }

Priority
^^^^^^^^

Der Priority Endpunkt ist unter der URL ``{BASE_URL}/api/v1/priority`` erreichbar.

1. Priority anzeigen:
    Durch einen GET-Resquest auf den Priority Endpunkt können die vorhandenen Priority abgefragt werden.

2. Informationen zu Priority anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/priority/{id}`` können Informationen zu einer Priority mit der gegebenen ID
    abgefragt werden.

3. Priority hinzufügen:
    Durch einen POST-Request auf den Priority Endpunkt können neue Priority erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit folgende Informationen enthalten:

    .. code-block::

      {
        "name": "Sehr niedrig",
        "value": 5
      },


4. Priority bearbeiten:
    Wie beim hinzufügen von neuen Priorities muss auch beim bearbeiten von bestehenden Priorities gültiges
    JSON mit den folgenden Informationen mittels PUT-Request an den Priority Endpunkt gesendet werden.

     .. code-block::

       {
        "id": "586fc3a1-bd87-4c90-94c1-b6816f6695b0",
        "name": "Sehr niedrig",
        "value": 10
        },

5. Priority löschen:
    Zum löschen einer Priority muss ein DELETE-Request an den Priority Endpunkt gesendet werden. Dieser muss
    die ID des zu löschenden Priority als JSON enthalten.

    .. code-block::

      {
        "id": "586fc3a1-bd87-4c90-94c1-b6816f6695b0",
      }

Procedure
^^^^^^^^^

Der Procedure Endpunkt ist unter der URL ``{BASE_URL}/api/v1/procedure`` erreichbar.

1. Procedure anzeigen:
    Durch einen GET-Resquest auf den Procedure Endpunkt können die vorhandenen Procedures abgefragt werden.

2. Informationen zu Procedure anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/procedure/{id}`` können Informationen zu einer Procedure mit der gegebenen ID
    abgefragt werden.

5. Procedure löschen:
    Zum löschen einer Procedure muss ein DELETE-Request an den Procedure Endpunkt gesendet werden. Dieser muss
    die ID des zu löschenden Procedure als JSON enthalten.

    .. code-block::

      {
        "id": "586fc3a1-bd87-4c90-94c1-b6816f6695b0",
      }

ProcessChain
^^^^^^^^^^^^

Der ProcessChain Endpunkt ist unter der URL ``{BASE_URL}/api/v1/processchain`` erreichbar.

1. ProcessChain anzeigen:
    Durch einen GET-Resquest auf den ProcessChain Endpunkt können die vorhandenen ProcessChain abgefragt werden.

2. Informationen zu ProcessChain anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/processchain/{id}`` können Informationen zu einer ProcessChain mit der gegebenen ID
    abgefragt werden.

3. ProcessChain hinzufügen:
    Durch einen POST-Request auf den ProcessChain Endpunkt können neue ProcessChain erstellt werden.
    Hierbei muss der Request Body gültiges JSON enthalten. Ein Beispiel für dieses JSON kann einem
    GET-Request entnommen werden.


4. ProcessChain bearbeiten:
    Wie beim hinzufügen von neuen Priorities muss auch beim bearbeiten von bestehenden Priorities gültiges
    JSON mittels PUT-Request an den ProcessChain Endpunkt gesendet werden.

5. ProcessChain löschen:
    Zum löschen einer ProcessChain muss ein DELETE-Request an den ProcessChain Endpunkt gesendet werden. Dieser muss
    die ID der zu löschenden ProcessChain als JSON enthalten.

    .. code-block::

      {
        "id": "586fc3a1-bd87-4c90-94c1-b6816f6695b0",
      }

ProcessStep
^^^^^^^^^^^

Der ProcessStep Endpunkt ist unter der URL ``{BASE_URL}/api/v1/processstep`` erreichbar.

1. ProcessSteps anzeigen:
    Durch einen GET-Resquest auf den ProcessStep Endpunkt können die vorhandenen ProcessSteps abgefragt werden.

2. Informationen zu ProcessSteps anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/processstep/{id}`` können Informationen zu einer ProcessStep mit der gegebenen ID
    abgefragt werden.

3. ProcessStep hinzufügen:
    Durch einen POST-Request auf den ProcessStep Endpunkt können neue ProcessStep erstellt werden.
    Hierbei muss der Request Body gültiges JSON enthalten. Ein Beispiel für dieses JSON kann einem
    GET-Request entnommen werden.


4. ProcessStep bearbeiten:
    Wie beim hinzufügen von neuen ProcessStep muss auch beim bearbeiten von bestehenden ProcessSteps gültiges
    JSON mittels PUT-Request an den ProcessSteps Endpunkt gesendet werden.

5. ProcessStep löschen:
    Zum löschen eines ProcessStep muss ein DELETE-Request an den Priority Endpunkt gesendet werden. Dieser muss
    die ID des zu löschenden ProcessSteps als JSON enthalten.

    .. code-block::

      {
        "id": "586fc3a1-bd87-4c90-94c1-b6816f6695b0",
      }

StateExecutions
^^^^^^^^^^^^^^^

Der StateExecution Endpunkt ist unter der URL ``{BASE_URL}/api/v1/stateexec`` erreichbar.

1. StateExecution anzeigen:
    Durch einen GET-Resquest auf den StateExecs Endpunkt können die vorhandenen StateExecs abgefragt werden.

2. Informationen zu StateExecutions anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/stateexec/{id}`` können Informationen zu einer StateExecs mit der gegebenen ID
    abgefragt werden.

StateHistory
^^^^^^^^^^^^

Der StateHistory Endpunkt ist unter der URL ``{BASE_URL}/api/v1/statehistory`` erreichbar.

1. StateHistory anzeigen:
    Durch einen GET-Resquest auf den StateHistory Endpunkt können die vorhandenen StateHistory abgefragt werden.

2. Informationen zu StateHistory anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/statehistory/{id}`` können Informationen zu einer StateHistory mit der gegebenen ID
    abgefragt werden.

5. StateHistory löschen:
    Zum löschen einer Priority muss ein DELETE-Request an den Priority Endpunkt gesendet werden. Dieser muss
    die ID des zu löschenden Priority als JSON enthalten.

    .. code-block::

      {
        "id": "586fc3a1-bd87-4c90-94c1-b6816f6695b0",
      }

StateMachine
^^^^^^^^^^^^

Der StateMachine Endpunkt ist unter der URL ``{BASE_URL}/api/v1/statemachine`` erreichbar.

1. StateMachine anzeigen:
    Durch einen GET-Resquest auf den StateMachine Endpunkt können die vorhandenen StateMachine abgefragt werden.

2. Informationen zu StateMachine anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/statemachine/{id}`` können Informationen zu einer StateMachine mit der gegebenen ID
    abgefragt werden.

3. StateMachine hinzufügen:
    Durch einen POST-Request auf den StateMachine Endpunkt können neue StateMachine erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit folgende Informationen enthalten:

    .. code-block::

      {
        "id": "2a8d93d1-61bd-4e65-a173-b2c4c33fe736",
        "name": "State Machine 1",
        "stateList": [
          {
            "id": "f922bf96-4839-4bf1-8db4-9d5644500a86",
            "name": "Erhitzen",
            "blocking": true
          },
          {
            "id": "afed7e96-e690-421d-beb9-e9adcec4bd0f",
            "name": "Einfrieren",
            "blocking": true
          },
          {
            "id": "69da7a90-1faf-4a4e-baa1-2c1fe017948e",
            "name": "Schütteln",
            "blocking": true
          },
          {
            "id": "a6a88df3-0a32-46ec-85f5-35cf27778707",
            "name": "Baden",
            "blocking": true
          },
          {
            "id": "d9790eca-cc56-4c39-8680-500bf10c6a0f",
            "name": "Abgeholt",
            "blocking": false
          }
        ]
      },


4. StateMachine bearbeiten:
    Wie beim hinzufügen von neuen StateMachines muss auch beim bearbeiten von bestehenden StateMachines gültiges
    JSON mit den folgenden Informationen mittels PUT-Request an den StateMachines Endpunkt gesendet werden.

     .. code-block::

        {
        "id": "2a8d93d1-61bd-4e65-a173-b2c4c33fe736",
        "name": "Zustandsautomat 1",
        "stateList": [
          {
            "id": "f922bf96-4839-4bf1-8db4-9d5644500a86",
            "name": "Erhitzen",
            "blocking": true
          },
          {
            "id": "afed7e96-e690-421d-beb9-e9adcec4bd0f",
            "name": "Einfrieren",
            "blocking": true
          },
          {
            "id": "69da7a90-1faf-4a4e-baa1-2c1fe017948e",
            "name": "Schütteln",
            "blocking": true
          },
          {
            "id": "a6a88df3-0a32-46ec-85f5-35cf27778707",
            "name": "Baden",
            "blocking": true
          },
          {
            "id": "d9790eca-cc56-4c39-8680-500bf10c6a0f",
            "name": "Abgeholt",
            "blocking": false
          }
        ]
      },

5. StateMachine löschen:
    Zum löschen einer StateMachine muss ein DELETE-Request an den StateMachine Endpunkt gesendet werden. Dieser muss
    die ID des zu löschenden StateMachine als JSON enthalten.

    .. code-block::

      {
        "id": "2a8d93d1-61bd-4e65-a173-b2c4c33fe736",
      }

State
^^^^^

Der State Endpunkt ist unter der URL ``{BASE_URL}/api/v1/state`` erreichbar.

1. States anzeigen:
    Durch einen GET-Resquest auf den States Endpunkt können die vorhandenen States abgefragt werden.

2. Informationen zu States anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/states/{id}`` können Informationen zu einer States mit der gegebenen ID
    abgefragt werden.

3. State hinzufügen:
    Durch einen POST-Request auf den States Endpunkt können neue States erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit folgende Informationen enthalten:

    .. code-block::

      {
        "name": "Abgeholt",
        "blocking": false
       },

4. State bearbeiten:
    Wie beim hinzufügen von neuen States muss auch beim bearbeiten von bestehenden States gültiges
    JSON mit den folgenden Informationen mittels PUT-Request an den States Endpunkt gesendet werden.

     .. code-block::

       {
        "id": "d9790eca-cc56-4c39-8680-500bf10c6a0f",
        "name": "Abgeholt",
        "blocking": true
        },

5. State löschen:
    Zum löschen einer State muss ein DELETE-Request an den State Endpunkt gesendet werden. Dieser muss
    die ID des zu löschenden State als JSON enthalten.

    .. code-block::

      {
        "id": "d9790eca-cc56-4c39-8680-500bf10c6a0f",
      }

Stock
^^^^^

Der Stock Endpunkt ist unter der URL ``{BASE_URL}/api/v1/stock`` erreichbar.

1. Stock anzeigen:
    Durch einen GET-Resquest auf den Stock Endpunkt können die vorhandenen Stocks abgefragt werden.

2. Informationen zu Stock anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/stock/{id}`` können Informationen zu einer Stocks mit der gegebenen ID
    abgefragt werden.

3. Stock hinzufügen:
    Durch einen POST-Request auf den Stock Endpunkt können neue Stock erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit folgende Informationen enthalten:

    .. code-block::

      {
        "position": "Lager"
       },

4. Stock bearbeiten:
    Wie beim hinzufügen von neuen Stock muss auch beim bearbeiten von bestehenden Stocks gültiges
    JSON mit den folgenden Informationen mittels PUT-Request an den Stocks Endpunkt gesendet werden.

     .. code-block::

       {
        "id": "d9790eca-cc56-4c39-8680-500bf10c6a0f",
        "position": "Lager 2"
        },

5. Stock löschen:
    Zum löschen einer Stock muss ein DELETE-Request an den Stock Endpunkt gesendet werden. Dieser muss
    die ID des zu löschenden Stock als JSON enthalten.

    .. code-block::

      {
        "id": "d9790eca-cc56-4c39-8680-500bf10c6a0f",
      }

ValidationPattern
^^^^^^^^^^^^^^^^^

Der ValidationPattern Endpunkt ist unter der URL ``{BASE_URL}/api/v1/validationpattern`` erreichbar.

1. ValidationPattern anzeigen:
    Durch einen GET-Resquest auf den ValidationPattern Endpunkt können die vorhandenen ValidationPatterns abgefragt werden.

4. ValidationPattern bearbeiten:
    Beim bearbeiten von bestehenden ValidationPattern muss gültiges JSON mit den folgenden Informationen mittels PUT-Request an den Stocks Endpunkt gesendet werden.

     .. code-block::

       {
        "name": "Username",
        "pattern": null,
        "smallCharacter": true,
        "capitalCharacter": true,
        "digits": true,
        "specialCharacters": false,
        "dot": true,
        "dash": true,
        "underscore": true,
        "slash": false,
        "backslash": false,
        "minLength": 3,
        "maxLength": 20,
        "space": false,
        "advanced": false
      }

Value
^^^^^

Der Value Endpunkt ist unter der URL ``{BASE_URL}/api/v1/value`` erreichbar.

1. Value anzeigen:
    Durch einen GET-Resquest auf den Value Endpunkt können die vorhandenen Value abgefragt werden.

2. Informationen zu Value anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/value/{id}`` können Informationen zu einer Value mit der gegebenen ID
    abgefragt werden.

3. Value hinzufügen:
    Durch einen POST-Request auf den Value Endpunkt können neue Value erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit folgende Informationen enthalten:

    .. code-block::

        {
            "value": "10",
            "parameter": {
              "id": "4c03dc40-0905-4f57-b7be-5e191637890c",
              "field": "Druck"
            }
        },

4. Value bearbeiten:
    Wie beim hinzufügen von neuen Value muss auch beim bearbeiten von bestehenden Value gültiges
    JSON mit den folgenden Informationen mittels PUT-Request an den Value Endpunkt gesendet werden.

     .. code-block::

       {
        "id": "b7ffe67d-ae8d-42e2-a724-f5704f56bc0f",
        "value": null,
        "parameter": {
        "id": "4c03dc40-0905-4f57-b7be-5e191637890c",
           "field": "Druck"
        }
       },

5. Value löschen:
    Zum löschen einer Value muss ein DELETE-Request an den Value Endpunkt gesendet werden. Dieser muss
    die ID des zu löschenden Value als JSON enthalten.

    .. code-block::

      {
        "id": "b7ffe67d-ae8d-42e2-a724-f5704f56bc0f",
      }

Workstation
^^^^^^^^^^^

Der Workstation Endpunkt ist unter der URL ``{BASE_URL}/api/v1/workstation`` erreichbar.

1. Workstation anzeigen:
    Durch einen GET-Resquest auf den Worksation Endpunkt können die vorhandenen Workstation abgefragt werden.

2. Informationen zu Workstation anzeigen:
    Über den Endpunkt ``{BASE_URL}/api/v1/workstation/{id}`` können Informationen zu einer Workstation mit der gegebenen ID
    abgefragt werden.

3. Workstation hinzufügen:
    Durch einen POST-Request auf den Workstation Endpunkt können neue Workstations erstellt werden.
    Hierbei muss der Request Body gültiges JSON mit folgende Informationen enthalten:

    .. code-block::

        {
            "name": "Workstation 1",
            "broken": true,
            "active": true,
            "users": [
              {
                "id": "da1d7232-391c-4013-ad01-d644680b3762",
                "username": "technologe",
                "firstName": "Thea",
                "lastName": "Technologin",
                "email": "technologe@kcb-test.de",
                "roles": [
                  "TECHNOLOGE"
                ]
              }
            ],
            "position": "Workstation 1"
        },

4. Workstation bearbeiten:
    Wie beim hinzufügen von neuen Workstation muss auch beim bearbeiten von bestehenden Workstation gültiges
    JSON mit den folgenden Informationen mittels PUT-Request an den Workstation Endpunkt gesendet werden.

     .. code-block::

      {
            "id": "1fbb5609-7fb9-4d47-a028-d57d86f4d754",
            "name": "Workstation 1",
            "broken": false,
            "active": true,
            "users": [
              {
                "id": "da1d7232-391c-4013-ad01-d644680b3762",
                "username": "technologe",
                "firstName": "Thea",
                "lastName": "Technologin",
                "email": "technologe@kcb-test.de",
                "roles": [
                  "TECHNOLOGE"
                ]
              }
            ],
            "position": "Workstation 1"
        },

5. Workstation löschen:
    Zum löschen einer Value muss ein DELETE-Request an den Workstation Endpunkt gesendet werden. Dieser muss
    die ID des zu löschenden Workstation als JSON enthalten.

    .. code-block::

      {
        "id": "1fbb5609-7fb9-4d47-a028-d57d86f4d754",
      }

