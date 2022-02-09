//
// Created by Ander on 26/01/2022.
//

#include "Workers.h"
# include <functional>
# include <iostream>
# include <list>
# include <mutex>
# include <thread>
# include <vector>
#include <condition_variable>

using namespace std;

condition_variable cv;

vector<thread> worker_threads;
list<function<void()>> tasks;
mutex tasks_mutex; // tasks mutex needed
int numOfThreads;
bool running;
bool gettingTask;

Workers::Workers(int i) {
    numOfThreads = i;
    running = true;

}


void Workers::start() {
    for (int j = 0; j < numOfThreads; j++) {
        gettingTask = true;
        worker_threads.emplace_back([] {
            while (running) {
                function<void()> task;
                {
                    unique_lock<mutex> lock(tasks_mutex);
                    while (gettingTask) {
                        cv.wait(lock);
                    }
                    if (!tasks.empty()) {
                        task = *tasks.begin(); // Copy task for later use
                        tasks.pop_front(); // Remove task from list
                    }
                }
                if (task) {
                    task(); // Run task outside mutex lock
                }

            }
        });

        {
            unique_lock<mutex> lock(tasks_mutex);
            gettingTask = false;
        }
        cv.notify_one();
    }
}

void Workers::post(const function<void()> &pFunction) {
    unique_lock<mutex> lock(tasks_mutex);
    tasks.emplace_back(pFunction);
    cv.notify_one();
}


void Workers::join() {
    for (thread &t: worker_threads) {
        t.join();
    }
}


void Workers::post_timeout(const function<void()> &pFunction) {
    unique_lock<mutex> lock(tasks_mutex);
    tasks.emplace_back([pFunction] {
        this_thread::sleep_for(1000ms);
        pFunction();
    });
    cv.notify_one();
}

void Workers::stop() {
    while (running) {
        if (tasks.empty()) {
            running = false;
            cv.notify_all();
        }
    }
}
