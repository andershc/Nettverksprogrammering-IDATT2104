//
// Created by Ander on 26/01/2022.
//

#ifndef OVING2_WORKERS_H
#define OVING2_WORKERS_H

#include <functional>

using namespace std;

class Workers {

public:
    explicit Workers(int i);

    void start();

    void post(const function<void()>& pFunction);

    void join();

    void stop();

    void post_timeout(const function<void()>& pFunction);
};


#endif //OVING2_WORKERS_H
